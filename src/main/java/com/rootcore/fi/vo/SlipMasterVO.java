package com.rootcore.fi.vo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SlipMasterVO {

    /** COMPANY_CODE VARCHAR2(20) NOT NULL */
    private String companyCode;

    /** SLIP_NO VARCHAR2(20) NOT NULL */
    private String slipNo;

    /** SLIP_DATE DATE DEFAULT SYSDATE NOT NULL */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate slipDate;

    /** FISCAL_PERIOD DATE DEFAULT SYSDATE NOT NULL */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fiscalPeriod;

    /** CUST_CODE VARCHAR2(20) */
    private String custCode;

    /** SLIP_TYPE VARCHAR2(20) */
    private String slipType;

    /** SUMMARY VARCHAR2(500) */
    private String summary;

    /** STATUS CHAR(1) DEFAULT 0 NOT NULL */
    private String status;

    /** INVOICE_NO VARCHAR2(20) */
    private String invoiceNo;

    /** HARP_NO VARCHAR2(20) */
    private String harpNo;

    /** CR_SUM NUMBER(15,2) */
    private double crSum;

    /** DR_SUM NUMBER(15,2) */
    private double drSum;

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
