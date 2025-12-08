package com.rootcore.fi.vo;

import java.util.List;

import lombok.Data;

/**
 * 세금계산서 저장 요청용 VO
 * - 화면에서 넘어오는 헤더 + 디테일 목록을 한 번에 받기 위한 VO 입니다.
 * - DB 테이블 매핑용 VO(TaxInvoiceVO, TaxInvoiceDetailVO)와는 별개입니다.
 */
@Data
public class TaxInvoiceSaveVO {

    /** 세금계산서 헤더 정보 */
    private TaxInvoiceVO header;

    /** 세금계산서 디테일 리스트 */
    private List<TaxInvoiceDetailVO> details;
}