package com.rootcore.hr.vo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.Data;

// 급여대장조히VO 급여대장등록VO
@Data
public class PayrollVO {
	
	private String payrollCode; // 급여대장코드
	private String companyCode; // 회사코드
	private String payrollPeriod; // 귀속연월
	private String payrollType; // 급여구분
	private String bonusMethod; // 상여지급방법
	private Long bonusRate; // 상여지급률
	private Long bonusAmount; // 상여지급액
	private String payrollName; // 대장명칭
	private Date payrollStartDate; // 대장기간시작일
	private Date payrollEndDate; // 대장기간종료일 
	private LocalDate payrollDate; // 지급일
	private int peopleNumber; // 인원수
	private List<String> employeeIds; // 사원ID배열집합
	private String userId; // 사원ID
	private String paymentStartDate; // 지급시작일
	private String paymentEndDate; // 지급종료일
	private String payrollPeriodCode; // 귀속연월코드
	
	
	


	
}
