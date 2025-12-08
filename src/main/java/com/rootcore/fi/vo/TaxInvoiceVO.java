package com.rootcore.fi.vo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class TaxInvoiceVO {

    /** COMPANY_CODE VARCHAR2(20) NOT NULL */
    private String companyCode;

    /** INVOICE_NO VARCHAR2(20) NOT NULL */
    private String invoiceNo;

    /** CUST_CODE VARCHAR2(20) */
    private String custCode;

    /** INVOICE_TYPE VARCHAR2(20) */
    private String invoiceType;

    /** STATUS CHAR(1) DEFAULT 0 NOT NULL */
    private String status;

    /** ISSUE_DATE DATE DEFAULT SYSDATE */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    /** DOCUMENT_DATE DATE DEFAULT SYSDATE */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate documentDate;

    /** TOTAL_SUPPLY_PRICE NUMBER(15,2) */
    private double totalSupplyPrice;

    /** TOTAL_TAX_PRICE NUMBER(15,2) */
    private double totalTaxPrice;

    /** TOTAL_AMOUNT NUMBER(15,2) */
    private double totalAmount;

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
