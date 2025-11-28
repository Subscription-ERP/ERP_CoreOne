package com.rootcore.hr.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CertificationVO {

	private Long certiSeq;           // 자격증순번
	private String companyCode;     // 회사코드
	private String userId;           // 사원번호
	private String certiName;        // 자격증명
	private String issueOrgName;    // 발급기관명
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date getDate;            // 취득일자
	private String licenseNo;        // 자격증번호
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date expireDate;         // 유효,만료일
	private String certiFile;        // 파일
	private String remark;            // 비고
	private String createdBy;
	private Date createDate;
	private String updatedBy;
	private Date updateDate;

}