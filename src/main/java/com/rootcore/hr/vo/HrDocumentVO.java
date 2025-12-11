package com.rootcore.hr.vo;

import java.util.Date;

import lombok.Data;

@Data
public class HrDocumentVO {

	// 증명서
	private String companyCode;      //회사코드
	private String docCode;          //증명서코드
	private String userId;           //사원번호_FK
	private String docType;          //증명서종류
	private Date issueDate;          //발급일
	private String purpose;          //용도
	private String createdBy;
	private Date createDate;
	private String updatedBy;
	private Date updateDate;
}
