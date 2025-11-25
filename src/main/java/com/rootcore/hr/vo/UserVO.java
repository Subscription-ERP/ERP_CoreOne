package com.rootcore.hr.vo;

import java.util.Date;

import lombok.Data;

// 급여대장-상여등록-사원조회VO
@Data
public class UserVO {
	
	private String userId; // 사원번호
	private String userName; // 성명
	private String dept; // 부서
	private Date hireDate; // 입사일
	private String jobTitle; // 직위/직급
}
