package com.rootcore.hr.vo;

import java.time.LocalDate;

import lombok.Data;

// 급여대장조회VO
@Data
public class PayrollVO {
	
	private String payrollPeriod; // 귀속연월
	private String 	payrollType; // 급여구분
	private String payrollName; // 대장명칭
	private LocalDate payrollDate; // 지급일
	private int peopleNumber; // 인원수
	
}
