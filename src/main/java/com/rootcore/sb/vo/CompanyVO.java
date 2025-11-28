package com.rootcore.sb.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
//domain = DB 테이블과 1ㄷ1 대응되는객체
//MyBatis mapper의 parameterType, resultType 으로 사용
public class CompanyVO {

    private String companyCode;      // 회사코드
    private String companyName;      // 회사명
    private String ceoName;          // 대표자명
    private String ceoPhone;         // 휴대폰번호
    private String companyEmail;     // 회사이메일
    private String industryType;     // 업종
    private String businessType;     // 업태
    private String managerName;      // 담당자 명
    private Integer employeeCount;   // 직원수
    private String bno;              // 사업자등록번호
    private String checkNo;          // 인증번호
    private String companyAddress;   // 회사 주소
    private String companyPhone;     // 회사 전화번호
    private String managerPhone;     // 담당자 연락처
    private String createdBy;        // 생성자
    private LocalDateTime createDate; // 생성일자
    private String updatedBy;        // 수정자
    private LocalDateTime updateDate; // 수정일자
}



















