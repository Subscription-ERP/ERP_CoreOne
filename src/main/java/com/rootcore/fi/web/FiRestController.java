package com.rootcore.fi.web;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.model.Model;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.fi.service.CreditService;
import com.rootcore.fi.service.HarpService;
import com.rootcore.fi.service.SlipService;
import com.rootcore.fi.service.TaxInvoiceService;
import com.rootcore.fi.service.UnitPriceService;
import com.rootcore.fi.vo.CreditVO;
import com.rootcore.fi.vo.HarpInvoiceVO;
import com.rootcore.fi.vo.HarpMasterVO;
import com.rootcore.fi.vo.MonthSlipVO;
import com.rootcore.fi.vo.SlipMasterVO;
import com.rootcore.fi.vo.TaxInvoiceSaveVO;
import com.rootcore.fi.vo.TaxInvoiceVO;
import com.rootcore.fi.vo.UnitPriceVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fi")
public class FiRestController {

    private final UnitPriceService unitPriceService;
    private final CreditService creditService;
    private final TaxInvoiceService taxInvoiceService;
    private final HarpService harpService;
    private final SlipService slipService; 

    //
    // 단가관리
    //
    @GetMapping("/unitprice")
    public List<UnitPriceVO> getUnitPriceList(UnitPriceVO param) {
        return unitPriceService.selectList(param);
    }

    @PostMapping("/unitprice")
    public int registUnitPrice(@RequestBody UnitPriceVO param) {
        return unitPriceService.insertUnitPrice(param);
    }

    @GetMapping("/unitprice/check")
    public Map<String, Object> unitPriceCheck(UnitPriceVO param) {
        int cnt = unitPriceService.checkUnitPrice(param);
        return Map.of("cnt", cnt);
    }

    @PutMapping("/unitprice")
    public int modifyUnitPrice(@RequestBody UnitPriceVO param) {
        return unitPriceService.updateUnitPrice(param);
    }

    //
    // 여신관리
    //
    @GetMapping("/credit")
    public List<CreditVO> getCreditList(CreditVO param) {
        System.out.println(param.getCustCode());
        return creditService.selectList(param);
    }

    @PutMapping("/credit")
    public int upsertCredit(@RequestBody CreditVO param) {
        return creditService.mergeCredit(param);
    }

    //
    // 세금계산서
    //

    /**
     * 세금계산서 + 전표 저장
     * - 요청: TaxInvoiceSaveVO (header + details[])
     * - 처리:
     *   1) 세금계산서 번호 생성 (함수 호출, Service 내부)
     *   2) TB_INVOICE_MASTER 저장
     *   3) TB_INVOICE_DETAIL 다건 저장
     *   4) TB_SLIP_MASTER / TB_SLIP_DETAIL 생성 및 저장
     * - 응답: invoiceNo, slipNo 반환
     */
    @PostMapping("/taxinvoice")
    public Map<String, Object> saveTaxInvoice(@RequestBody TaxInvoiceSaveVO param) {

        Map<String, Object> result = new HashMap<>();

        try {
        	System.out.println(param);
            // Service에서 전체 트랜잭션 처리 (세금계산서 + 전표)
            Map<String, Object> rtn = taxInvoiceService.saveTaxInvoice(param);

            result.put("success", true);
            result.put("invoiceNo", rtn.get("invoiceNo"));
            result.put("slipNo", rtn.get("slipNo"));

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }

    /**
     * 세금계산서 번호 생성 API
     * - Oracle 함수(FN_MAKE_INVOICE_NO 같은 것)를 TaxInvoiceService에서 호출
     * - 화면에서 "번호 미리 발급" 용도로 사용 가능
     */
    @GetMapping("/taxinvoice/no")
    public Map<String, Object> getInvoiceNo() {

        Map<String, Object> result = new HashMap<>();

        try {
            String invoiceNo = taxInvoiceService.makeInvoiceNo();
            result.put("success", true);
            result.put("invoiceNo", invoiceNo);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }
    
    //
    // 수금등록
    //

    /**
     * 수금 대상 세금계산서 목록 조회
     * - 요청: companyCode, custCode (쿼리스트링 → HarpInvoiceVO 필드로 바인딩)
     * - 응답: HarpInvoiceVO 리스트
     */
    @GetMapping("/harp/invoice")
    public List<HarpInvoiceVO> getHarpInvoiceList(HarpInvoiceVO param) {
        // param.companyCode, param.custCode 가 JS에서 넘긴 값으로 들어옵니다.
        return harpService.selectInvoiceTargetList(param);
    }

    /**
     * 수금정보 저장
     * - 요청: HarpMasterVO (TB_HARP_MASTER 기준 + detailList<List<HarpDetailVO>>)
     * - 처리: Service에서 HARP + 전표까지 트랜잭션 처리
     * - 응답: { success, harpNo, slipNo?, message }
     */
    @PostMapping("/harp")
    public Map<String, Object> saveHarp(@RequestBody HarpMasterVO param) {

        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> rtn = harpService.saveHarp(param);

            result.put("success", true);
            result.put("harpNo", rtn.get("harpNo"));   // 수금번호
            result.put("slipNo", rtn.get("slipNo"));   // 전표번호 (전표 연동 시)

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }
    //
    //세금계산서 조회
    //
    @GetMapping("/taxinvoicehistory")
    public List<TaxInvoiceVO> getTaxInvoiceHistory(
            @RequestParam("companyCode") String companyCode,
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam("toDate")   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam(value = "custCode", required = false) String custCode
    ) {
        return taxInvoiceService.getTaxInvoiceHistory(companyCode, fromDate, toDate, custCode);
    }
    //
    // 수동 전표등록
    //
    /**
     * 수동 전표 등록
     * - 요청: SlipMasterVO (헤더 + detailList<List<SlipDetailVO>> 포함 형태를 권장)
     * - 처리: Service에서 전표번호 생성, 마스터/디테일 저장 트랜잭션 처리
     * - 응답: { success, slipNo, message }
     */
    @PostMapping("/slip")
    public Map<String, Object> saveSlip(@RequestBody SlipMasterVO param) {

        Map<String, Object> result = new HashMap<>();

        try {
            // slipService에서 전표번호 생성 + 마스터/디테일 저장까지 트랜잭션 처리
            Map<String, Object> rtn = slipService.saveManualSlip(param);

            result.put("success", true);
            result.put("slipNo", rtn.get("slipNo"));   // 생성된 전표번호

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }

    /**
     * 월별 분개장 조회
     */
    @GetMapping("/monthslip")
    public List<MonthSlipVO> getMonthSlipList(
            @RequestParam("yearMonth") String yearMonth,
            @RequestParam(value = "accountCode", required = false) String accountCode
    ) {
        return slipService.getMonthSlipList(yearMonth, accountCode);
    }
	@GetMapping("/fi/taxinvoice/print")
	public String printTaxInvoice(
	        @RequestParam("invoiceNos") String invoiceNos,
	        Model model) {

	    // 서비스 호출하여 여러 건 출력 데이터 조회
//	    List<TaxInvoiceVO> invoices = taxInvoiceService.getInvoicePrintData(invoiceNos);

//	    model.addAttribute("invoices", invoices);

	    // 출력 화면으로 이동
	    return "fi/taxInvoicePrint";
	}
}
