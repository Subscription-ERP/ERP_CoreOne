package com.rootcore.fi.vo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class TaxInvoiceDetailVO {

    /** INVOICE_DETAIL_NO VARCHAR2(20) NOT NULL */
    private String invoiceDetailNo;

    /** COMPANY_CODE VARCHAR2(20) NOT NULL */
    private String companyCode;

    /** INVOICE_NO VARCHAR2(20) NOT NULL */
    private String invoiceNo;

    /** SKU VARCHAR2(20) NOT NULL */
    private String sku;

    /** QTY NUMBER(10) */
    private int qty;
    
    private String inordNo;
    private String inordDetailNo;

    /** UNIT_PRICE NUMBER(15,2) */
    private double unitPrice;

    /** SUPPLY_PRICE NUMBER(15,2) */
    private double supplyPrice;

    /** TAX_PRICE NUMBER(15,2) */
    private double taxPrice;

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
