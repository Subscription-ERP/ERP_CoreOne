package com.rootcore.hr.vo;

import java.sql.Date;

import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserPayManageVO {
	private String user_id; // 사번
	private String user_name; // 사원이름
	private String dept_name; // 부서이름
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date payroll_date; // 지급일
	private Long salary; // 기본급
	private Long bonus; // 상여
	private Long overtime; // 총연장근무시간
	private Long night; // 총야간근무시간
	private Long holiday; // 총휴일근무시간
	private Long family; // 가족수
	private Long meal; // 식대
	private Long annual_leave; // 연차휴가수당
	private Long total_allowance; // 총 수당총액(상여 미포함)
	private Long total_payment_amount; // 총 지급액(상여포함)
	private Long national_pension; // 국민연금
	private Long employment_insurance; // 고용보험
	private Long health_insurance; // 건강보험
	private Long long_time_care_insurance; // 장기요양보험
	private Long total_deduction_amount; // 총 4대보험(소득세랑 지방소득세가 없어서 지금은 이게 공제 총액)
	private Long net_pay; // 실 수령액
}
