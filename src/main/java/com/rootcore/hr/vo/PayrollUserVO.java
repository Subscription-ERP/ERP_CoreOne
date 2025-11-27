package com.rootcore.hr.vo;

import java.time.LocalDate;

import lombok.Data;

//급여대장-상여등록-사원조회VO
@Data
public class PayrollUserVO {
	
	private String userId; // 사원번호
	private String userName; // 성명
	private String dept; // 부서
	private LocalDate hireDate; // 입사일
	private String jobTitle; // 직위/직급
}
