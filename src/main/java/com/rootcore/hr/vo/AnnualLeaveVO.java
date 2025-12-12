package com.rootcore.hr.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class AnnualLeaveVO {
	private String annualLeaveCode;  // 연차관리번호
    private String companyCode;      // 회사코드
    private String userId;           // 사원번호
    private String grantYear;        // 부여연도
    private double totalGrantDays;   // 발생연차 (총 발생일수)
    private double totalUsedDays;    // 사용일수 (총 사용일수)
    private double remainingDays;    // 잔여연차 (남은 일수)
    private Date expiryDate;         // 소멸예정일
    private String deptName; // 부서이름
    private String userName; // 사원이름
    private String jobTitle; // 직위직급
    
    // 검색조건
    private double remainingDaysStart; // 잔여연차범위시작
    private double remainingDaysEnd; // 잔여연차범위종료
    private String deptCode; // 부서코드
    
}
