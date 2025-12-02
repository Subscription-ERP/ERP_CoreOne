package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class MenuPermissionUserVO {

    private String companyCode;   // 회사 코드
    private String userId;        // 로그인ID
    private String userName;      // 성명
    private String deptName;      // 부서명
    private String positionName;  // 직급
    private String status;        // 재직/휴직/퇴사
}
