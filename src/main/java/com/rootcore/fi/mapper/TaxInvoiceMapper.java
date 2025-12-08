package com.rootcore.fi.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.rootcore.fi.vo.TaxInvoiceDetailVO;
import com.rootcore.fi.vo.TaxInvoiceVO;

@Mapper
public interface TaxInvoiceMapper {

    /** 세금계산서 번호 생성 (함수 호출) */
    String makeInvoiceNo();

    /** 세금계산서 마스터 저장 */
    int insertInvoiceMaster(TaxInvoiceVO vo);

    /** 세금계산서 디테일 저장 */
    int insertInvoiceDetail(TaxInvoiceDetailVO vo);
}
