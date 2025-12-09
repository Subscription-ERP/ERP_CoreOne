package com.rootcore.fi.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 수금 상세 VO
 * 기준 테이블: TB_HARP_DETAIL
 */
@Data
public class HarpDetailVO {

    // === TB_HARP_DETAIL 컬럼 매핑 필드 ===

    /** 수금상세번호 (HARP_DETAIL_NO) */
    private String harpDetailNo;

    /** 회사코드 (COMPANY_CODE) */
    private String companyCode;

    /** 수금번호 (HARP_NO) */
    private String harpNo;

    /** 세금계산서번호 (INVOICE_NO) */
    private String invoiceNo;

    /** 수금금액 (AMOUNT, NUMBER(15,2)) */
    private double amount;

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

    /** 세금계산서 발행일자 (TB_INVOICE_MASTER.ISSUE_DATE 조인용) */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date issueDate;

    /** 세금계산서 합계금액 (TB_INVOICE_MASTER.TOTAL_AMOUNT, NUMBER(15,2)) */
    private double totalAmount;

    /** 해당 세금계산서의 기존 수금합계 (SUM(TB_HARP_DETAIL.AMOUNT)) */
    private double harpSum;

    /** 미수금액 = totalAmount - harpSum */
    private double remainAmount;

    /** 그리드에서 선택 여부 (체크박스용) */
    private boolean checked;
}
