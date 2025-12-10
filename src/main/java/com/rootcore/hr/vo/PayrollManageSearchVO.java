package com.rootcore.hr.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class PayrollManageSearchVO {
	private String companyCode; // 회사코드
	private String userId; // 사원id
	private String dept; // 부서명
	private String userName; // 성명
	private String payPeriodStart; // 귀속연월시작
	private String payPeriodEnd; // 귀속연월종료
	
}
