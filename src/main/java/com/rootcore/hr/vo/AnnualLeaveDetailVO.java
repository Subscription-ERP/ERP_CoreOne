package com.rootcore.hr.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class AnnualLeaveDetailVO {
	// 사원 기본 정보
	private String userId; // 사번
	private String userName; // 성명
	private String deptName; // 부서명
	private String jobTitle; // 직위직급 (job_title)

	// 연차 사용 상세 정보 (TB_ANNUAL_LEAVE_DETAIL 연계)
	private String leaveType; // 신청구분 (leave_type)
	private double usedDays; // 사용일수 (used_days)
	private Date annualStartDate; // 연차시작일 (leave_start_date)
	private Date annualEndDate; // 연차종료일 (leave_end_date)
	private Date leaveApplyDate; // 연차신청일 (leave_apply_date)
	private String rm; // 사유 (rm)
	
	private String annualLeaveCode;  // 연차관리코드
	private Long annualLeaveDetailSeq; // 연차상세관리번호
}
