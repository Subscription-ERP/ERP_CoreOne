package com.rootcore.hr.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/* ==================
 * tb_dept_master VO
 * 부서 마스터 테이블 VO
 * ================== */
@Data
public class DeptMasterVO {
	private String userId; 
	private String deptCode;
	private String companyCode;
	private String deptName;
	private String upperDeptNo;
	private String upperDeptName;
	private String deptLevel; // 부서 레벨
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	private String status;
	private String rm; // 비고

	private String text;
	private String deptCodeManageStartLevel; // 적용시작일
	private String deptCodeManageEndLevel;
	

}