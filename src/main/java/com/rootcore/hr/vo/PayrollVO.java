package com.rootcore.hr.vo;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class PayrollVO {
	
	private String payrollPeriod; // 귀속연월
	private String 	payrollType; // 급여구분
	private String payrollName; // 대장명칭
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String payrollDate; // 지급일
	private int peopleNumber; // 인원수
	
}
