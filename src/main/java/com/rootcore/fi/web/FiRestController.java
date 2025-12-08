package com.rootcore.fi.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.fi.service.CreditService;
import com.rootcore.fi.service.TaxInvoiceService;
import com.rootcore.fi.service.UnitPriceService;
import com.rootcore.fi.vo.CreditVO;
import com.rootcore.fi.vo.TaxInvoiceSaveVO;
import com.rootcore.fi.vo.UnitPriceVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fi")
public class FiRestController {

    private final UnitPriceService unitPriceService;
    private final CreditService creditService;
    private final TaxInvoiceService taxInvoiceService;

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
}
