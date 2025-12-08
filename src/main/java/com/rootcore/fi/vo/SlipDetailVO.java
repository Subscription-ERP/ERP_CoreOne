package com.rootcore.fi.vo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SlipDetailVO {

    /** SLIP_DETAIL_NO VARCHAR2(20) NOT NULL */
    private String slipDetailNo;

    /** COMPANY_CODE VARCHAR2(20) NOT NULL */
    private String companyCode;

    /** SLIP_NO VARCHAR2(20) NOT NULL */
    private String slipNo;

    /** DR_CR_TYPE CHAR(1) */
    private String drCrType;

    /** SLIP_ACCOUNT VARCHAR2(20) */
    private String slipAccount;

    /** AMOUNT NUMBER(15,2) */
    private double amount;

    /** CREATED_BY VARCHAR2(20) DEFAULT 'ADMIN' */
    private String createdBy;

    /** CREATE_DATE DATE DEFAULT SYSDATE */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    /** UPDATED_BY VARCHAR2(20) DEFAULT 'ADMIN' */
    private String updatedBy;

    /** UPDATE_DATE DATE DEFAULT SYSDATE */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updateDate;
}
