package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class LoginUserVO {

    private String companyCode;
    private String userId;
    private String userName;

    // ✅ 부서/직급 추가
    private String dept;
    private String position;

    private String roleCode;   // DB 컬럼 ROLE_CODE
}
