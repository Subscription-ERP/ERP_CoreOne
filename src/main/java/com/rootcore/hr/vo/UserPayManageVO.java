package com.rootcore.hr.vo;

import lombok.Data;

@Data
public class UserPayManageVO {
	private String user_id;
	private Long salary;
	private Long bonus;
	private Long overtime;
	private Long night;
	private Long holiday;
	private Long family;
	private Long meal;
	private Long annual_leave;
	private Long total_allowance;
	private Long total_payment_amount;
	private Long national_pension;
	private Long employment_insurance;
	private Long health_insurance;
	private Long long_time_care_insurance;
	private Long total_deduction_amount;
	private Long net_pay;
}
