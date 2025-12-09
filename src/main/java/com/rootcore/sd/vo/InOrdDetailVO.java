package com.rootcore.sd.vo;

import lombok.Data;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class InOrdDetailVO {
    private String companyCode;         // 회사코드
    private String inordNo;             // 수주번호
    private String inordDetailNo;       // 수주세부번호
    private String sku;                 // 품목코드
    private String skuName;				// 품목명
    private int qty;                    // 수량
    private double unitPrice;           // 단가
    private double supplyPrice;         // 공급가액
    private double surtax;              // 부가세
    private double price;               // 가격(공급가액+부가세)
    private String remark;              // 비고
    private String outputStatus;        // 출고여부
    private String outputStatusName;    // 출고여부 이름
    private String invoiceStatus;       // 세금계산서 발행여부
    private String invoiceStatusName;   // 세금계산서 발행여부 이름
    private String createdBy;           // 생성자
    private Date createDate;            // 생성일자
    private String updatedBy;           // 수정자
    private Date updateDate;            // 수정일자
    
    // 수주 마스터정보 추가
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date inordDate;
	private String custCode;
	private String custName;
	
	// 조회구분 추가
	private String searchDiv;
	
}
