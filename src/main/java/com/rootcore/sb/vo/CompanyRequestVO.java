package com.rootcore.sb.vo;

import lombok.Data;

//프론트에서 전달받는 데이터 구조
@Data
public class CompanyRequestVO {
	  private String companyName;     // 회사명
	    private String ceoName;         // 대표자명
	    private String ceoPhone;        // 휴대폰번호
	    private String companyEmail;    // 회사이메일
	    private String industryType;    // 업종
	    private String businessType;    // 업태
	    private String managerName;     // 담당자 명
	    private Integer employeeCount;  // 직원 수
	    private String bno;             // 사업자등록번호
	    private String checkNo;         // 인증번호
	    private String companyAddress;  // 회사 주소
	    private String companyPhone;    // 회사 전화번호
	    private String managerPhone;    // 담당자 연락처

}
