package com.rootcore.fi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.rootcore.fi.vo.TaxInvoiceSaveVO;
import com.rootcore.fi.vo.TaxInvoiceVO;

public interface TaxInvoiceService {

    /**
     * 세금계산서 + 전표 저장
     * @param param (header + details)
     * @return { invoiceNo, slipNo }
     * @throws Exception
     */
    Map<String, Object> saveTaxInvoice(TaxInvoiceSaveVO param) throws Exception;

    /**
     * 세금계산서 번호 생성 (함수 호출)
     * @return invoiceNo
     * @throws Exception
     */
    String makeInvoiceNo() throws Exception;
    /**
     * 세금계산서 발행현황 조회
     *
     * @param companyCode 회사코드
     * @param fromDate    발행일자 FROM (yyyy-MM-dd)
     * @param toDate      발행일자 TO   (yyyy-MM-dd)
     * @param custCode    거래처코드 (옵션, null 가능)
     * @return 조건에 맞는 세금계산서 리스트
     */
    List<TaxInvoiceVO> getTaxInvoiceHistory(String companyCode,
            LocalDate fromDate,
            LocalDate toDate,
            String custCode);
}
