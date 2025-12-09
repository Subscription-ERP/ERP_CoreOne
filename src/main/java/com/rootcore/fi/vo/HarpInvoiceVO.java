package com.rootcore.fi.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 수금등록용 세금계산서 조회 VO
 * 기준 테이블: TB_INVOICE_MASTER
 */
@Data
public class HarpInvoiceVO {

    // === TB_INVOICE_MASTER 컬럼 매핑 필드 ===

    /** 회사코드 (COMPANY_CODE) */
    private String companyCode;

    /** 세금계산서번호 (INVOICE_NO) */
    private String invoiceNo;

    /** 거래처코드 (CUST_CODE) */
    private String custCode;

    /** 등록유형 (INVOICE_TYPE) */
    private String invoiceType;

    /** 세금계산서상태 (STATUS) */
    private String status;

    /** 발행일자 (ISSUE_DATE) */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date issueDate;

    /** 작성일자 (DOCUMENT_DATE) */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date documentDate;

    /** 공급가액총액 (TOTAL_SUPPLY_PRICE, NUMBER(15,2)) */
    private double totalSupplyPrice;

    /** 세액총액 (TOTAL_TAX_PRICE, NUMBER(15,2)) */
    private double totalTaxPrice;

    /** 합계금액 (TOTAL_AMOUNT, NUMBER(15,2)) */
    private double totalAmount;

    /** 생성자 (CREATED_BY) */
    private String createdBy;

    /** 생성일자 (CREATE_DATE) */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createDate;

    /** 수정자 (UPDATED_BY) */
    private String updatedBy;

    /** 수정일자 (UPDATE_DATE) */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date updateDate;

    // === VO 전용 필드(테이블에는 없음) ===

    /** 거래처명 (TB_CUST_MASTER.CUST_NAME) */
    private String custName;

    /** 지금까지 수금된 금액 합계 (SUM(TB_HARP_DETAIL.AMOUNT)) */
    private double harpAmount;

    /** 미수금액 = totalAmount - harpAmount */
    private double remainAmount;

    /** 이번 수금등록에서 입력한 수금금액 (grid 수금 입력 필드) */
    private double inputHarpAmount;
}
