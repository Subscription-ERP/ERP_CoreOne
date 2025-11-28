package com.rootcore.hr.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserHistoryVO {

	private Long ehistSeq;                // 이력순번
	private String companyCode;           // 회사코드
	private String userId;                // 사원번호
	private String histType;              // 이력타입
	private String histTypeName;         // 이력타입명
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date applyDate;               // 적용일자
	private String prevDept;              // 이전부서
	private String prevDeptName;         // 이전부서명
	private String prevJobTitle;         // 이전 직위,직급
	private String prevJobTitleName;    // 이전 직위,직급명
	private String newDept;               // 변경부서
	private String newDeptName;          // 변경부서명
	private String newJobTitle;          // 변경 직위,직급
	private String newJobTitleName;     // 변경 직위,직급명
	private String deptChangeReason;     // 변경사유
	private Long baseSalary;              // 기본급
	private Long totalSalary;             // 총급여
	private String payType;               // 지급유형
	private String payTypeName;          // 지급유형명
	private String salaryChangeReason;   // 변경사유
	private String createdBy;            
	private Date createDate;
	private String updatedBy;
	private Date updateDate;
	
}
