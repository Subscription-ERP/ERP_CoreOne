package com.rootcore.fi.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 수금 마스터 VO
 * 기준 테이블: TB_HARP_MASTER
 */
@Data
public class HarpMasterVO {

    // === TB_HARP_MASTER 컬럼 매핑 필드 ===

    /** 회사코드 (COMPANY_CODE) */
    private String companyCode;

    /** 수금번호 (HARP_NO) */
    private String harpNo;

    /** 거래처코드 (CUST_CODE) */
    private String custCode;

    /** 수금일자 (HARP_DATE) */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date harpDate;

    /** 총수금금액 (TOTAL_AMOUNT, NUMBER(15,2)) */
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

    /** 수금 상세 목록 (TB_HARP_DETAIL 리스트) */
    private List<HarpDetailVO> detailList;

    /** 거래처명 (TB_CUST_MASTER.CUST_NAME 조인용) */
    private String custName;

    /** 수금일자 조회 시작일 (검색 조건용) */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date harpDateFrom;

    /** 수금일자 조회 종료일 (검색 조건용) */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date harpDateTo;
}
