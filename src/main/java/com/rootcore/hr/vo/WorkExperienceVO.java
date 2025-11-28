package com.rootcore.hr.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class WorkExperienceVO {

	private Long wexSeq;                // 경력사항순번
	private String companyCode;         // 회사코드
	private String userId;              // 사원번호
	private String wexCompanyName;      // 회사명
	private String wexDept;             // 부서명
	private String wexJobTitle;         // 직위,직급명
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date wexHireDate;           // 입사일자
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date wexLeaveDate;          // 퇴사일자
	private String wexMainDuty;         // 담당업무
	private Long wexSalary;             // 최종연봉
	private String remark;              // 비고
	private String createdBy;
	private Date createDate;
	private String updatedBy;
	private Date updateDate;
	
}
