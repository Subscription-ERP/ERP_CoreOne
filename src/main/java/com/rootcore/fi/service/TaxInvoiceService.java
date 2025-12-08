package com.rootcore.fi.service;

import java.util.Map;

import com.rootcore.fi.vo.TaxInvoiceSaveVO;

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
}
