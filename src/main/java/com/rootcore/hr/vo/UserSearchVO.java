package com.rootcore.hr.vo;

import lombok.Data;

@Data
public class UserSearchVO {

	// 사원관리 검색 VO
	private String companyCode;  // 로그인한 회사코드
	
    private String keyName;      // 성명
    private String deptSearch;   // 부서
    private String leavedYN;     // 퇴사자 포함 여부
	
}
