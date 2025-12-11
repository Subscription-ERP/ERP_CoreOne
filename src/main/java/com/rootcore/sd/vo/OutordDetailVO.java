package com.rootcore.sd.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OutordDetailVO {
    private String companyCode;         // 회사코드
    private String outordNo;            // 발주번호
    private String outordDetailNo;      // 발주세부번호
    private String sku;                 // 품목코드
    private String skuName;             // 품목명
    private int qty;                    // 수량
    private double unitPrice;           // 단가
    private double supplyPrice;         // 공급가액
    private double surtax;              // 부가세
    private double price;               // 가격(공급가액+부가세)
    private String remark;              // 비고
    private String inputStatus;         // 입고여부
    private String inputStatusName;     // 입고여부 이름
    private String createdBy;           // 생성자
    private Date createDate;            // 생성일자
    private String updatedBy;           // 수정자
    private Date updateDate;            // 수정일자
}
