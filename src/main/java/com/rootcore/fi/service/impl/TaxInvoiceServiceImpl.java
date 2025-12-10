package com.rootcore.fi.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.fi.mapper.SlipMapper;
import com.rootcore.fi.mapper.TaxInvoiceMapper;
import com.rootcore.fi.service.TaxInvoiceService;
import com.rootcore.fi.vo.SlipDetailVO;
import com.rootcore.fi.vo.SlipMasterVO;
import com.rootcore.fi.vo.TaxInvoiceDetailVO;
import com.rootcore.fi.vo.TaxInvoiceSaveVO;
import com.rootcore.fi.vo.TaxInvoiceVO;
import com.rootcore.sd.mapper.InOrdMapper;
import com.rootcore.sd.vo.InOrdDetailVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaxInvoiceServiceImpl implements TaxInvoiceService {

    private final TaxInvoiceMapper taxInvoiceMapper;
    private final SlipMapper slipMapper;
    private	final InOrdMapper inOrdMapper;
    /**
     * 세금계산서 + 전표 저장 (트랜잭션)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveTaxInvoice(TaxInvoiceSaveVO param) throws Exception {

        if (param == null) {
            throw new IllegalArgumentException("요청 데이터가 없습니다.");
        }

        TaxInvoiceVO header = param.getHeader();
        List<TaxInvoiceDetailVO> details = param.getDetails();
        if (header == null) {
            throw new IllegalArgumentException("세금계산서 헤더 정보가 없습니다.");
        }
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("세금계산서 디테일 정보가 없습니다.");
        }

        // 1. 세금계산서 번호 생성 (함수 호출)
        String invoiceNo = taxInvoiceMapper.makeInvoiceNo();
        header.setInvoiceNo(invoiceNo);

        // 2. 합계 재계산 (안전하게 서버에서 다시 계산)
        double totalSupply = 0d;
        double totalTax = 0d;

        for (TaxInvoiceDetailVO d : details) {
            if (d == null) continue;

            totalSupply += d.getSupplyPrice();  // primitive double 이면 null 걱정 없음
            totalTax += d.getTaxPrice();
        }

        header.setTotalSupplyPrice(totalSupply);
        header.setTotalTaxPrice(totalTax);
        header.setTotalAmount(totalSupply + totalTax);

        // 상태값 기본값 (필요 시) - DB default '0' 도 있으니 상황에 맞게 사용
        if (header.getStatus() == null || header.getStatus().isEmpty()) {
            header.setStatus("0");
        }

        // 3. TB_INVOICE_MASTER INSERT
        taxInvoiceMapper.insertInvoiceMaster(header);

        // 4. TB_INVOICE_DETAIL INSERT (간단히 invoiceNo + 순번으로 상세번호 생성)
        int seq = 1;
        for (TaxInvoiceDetailVO d : details) {
            if (d == null) continue;

            d.setCompanyCode(header.getCompanyCode());
            d.setInvoiceNo(invoiceNo);

            if (d.getInvoiceDetailNo() == null || d.getInvoiceDetailNo().isEmpty()) {
                // 예: 202412000001 + 001 → 202412000001001
                d.setInvoiceDetailNo(""+seq);
            }

            taxInvoiceMapper.insertInvoiceDetail(d);
            seq++;
        }

        // 5. 전표번호 생성 (함수 사용)
        String slipNo = slipMapper.makeSlipNo();

        // 6. TB_SLIP_MASTER INSERT
        SlipMasterVO sm = new SlipMasterVO();
        sm.setCompanyCode(header.getCompanyCode());
        sm.setSlipNo(slipNo);
        sm.setSlipDate(header.getDocumentDate()); 
        sm.setFiscalPeriod(header.getDocumentDate()); // 회계일자도 우선 동일하게
        sm.setCustCode(header.getCustCode());
        sm.setSlipType(header.getInvoiceType());   // 전표유형 = 세금계산서유형 사용(또는 고정코드로 변경 가능)
        sm.setSummary("세금계산서 발행 - " + invoiceNo);
        sm.setStatus("0");
        sm.setInvoiceNo(invoiceNo);
        sm.setHarpNo(null); // 수금번호는 아직 없음

        sm.setDrSum(header.getTotalAmount());
        sm.setCrSum(header.getTotalAmount());

        slipMapper.insertSlipMaster(sm);

        // 7. TB_SLIP_DETAIL INSERT
        //    ※ 계정코드는 예시입니다. 실제 프로젝트 계정코드에 맞게 수정하셔야 합니다.

        // (1) 차변: 매출채권(또는 외상매출금) - 총액(공급가액 + 세액)
        SlipDetailVO sd1 = new SlipDetailVO();
        sd1.setSlipDetailNo(slipNo + "001");
        sd1.setCompanyCode(sm.getCompanyCode());
        sd1.setSlipNo(slipNo);
        sd1.setDrCrType("D");
        sd1.setSlipAccount("110000");  // TODO: 실제 '매출채권' 계정코드로 변경
        sd1.setAmount(header.getTotalAmount());
        slipMapper.insertSlipDetail(sd1);

        // (2) 대변: 매출 - 공급가액
        SlipDetailVO sd2 = new SlipDetailVO();
        sd2.setSlipDetailNo(slipNo + "002");
        sd2.setCompanyCode(sm.getCompanyCode());
        sd2.setSlipNo(slipNo);
        sd2.setDrCrType("C");
        sd2.setSlipAccount("410000");  // TODO: 실제 '매출' 계정코드로 변경
        sd2.setAmount(header.getTotalSupplyPrice());
        slipMapper.insertSlipDetail(sd2);

        // (3) 대변: 부가세예수금 - 세액
        SlipDetailVO sd3 = new SlipDetailVO();
        sd3.setSlipDetailNo(slipNo + "003");
        sd3.setCompanyCode(sm.getCompanyCode());
        sd3.setSlipNo(slipNo);
        sd3.setDrCrType("C");
        sd3.setSlipAccount("210000");  // TODO: 실제 '부가세예수금' 계정코드로 변경
        sd3.setAmount(header.getTotalTaxPrice());
        slipMapper.insertSlipDetail(sd3);

        for(TaxInvoiceDetailVO d : details) {
	        InOrdDetailVO io = new InOrdDetailVO();
	        io.setInordNo(d.getInordNo());
	        io.setInordDetailNo(d.getInordDetailNo());
	        inOrdMapper.updateInordInvoice(io);
        }
        
        
        
        // 8. 리턴값 구성
        Map<String, Object> rtn = new HashMap<>();
        rtn.put("invoiceNo", invoiceNo);
        rtn.put("slipNo", slipNo);

        return rtn;
    }

    /**
     * 세금계산서 번호 생성 (함수 호출)
     */
    @Override
    public String makeInvoiceNo() throws Exception {
        return taxInvoiceMapper.makeInvoiceNo();
    }

	@Override
	public List<TaxInvoiceVO> getTaxInvoiceHistory(String companyCode, LocalDate fromDate, LocalDate toDate, String custCode) {
		// TODO Auto-generated method stub
	    LocalDate toDateForQuery = toDate;

	    if (toDateForQuery != null) {
	        // plusDays(1)의 결과를 다시 변수에 담아야 실제로 +1일 된 값이 사용됩니다.
	        toDateForQuery = toDateForQuery.plusDays(1);
	    }
        return taxInvoiceMapper.selectTaxInvoiceHistory(companyCode, fromDate, toDateForQuery, custCode);
	}
}
