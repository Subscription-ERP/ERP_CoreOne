package com.rootcore.hr.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/* ====================
 * 사원급여관리 상세조회 VO
 * ==================== */
@Data
public class UserPayManageDetailVO {
	private String userPayManagementCode;
	private String userName;
	private String userId;
	private String deptName;
	private String codeName;
	private String salary;
	private String bonus;
	private String overtimeAllowance;
	private String nightAllowance;
	private String holidayAllowance;
	private String familyAllowance;
	private String mealAllowance;
	private String annualLeaveAllowance;
	private String totalPayment;
	private String nationalPension;
	private String employmentInsurance;
	private String healthInsurance;
	private String longTimeCareInsurance;
	private String totalDeduction;
	private String netPay;
	private String payrollCode;
	private String companyCode;
	private String payPeriod; // 귀속연월
}
