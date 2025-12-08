package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class UserAuthVO {

    private String companyCode;

    private String userId;      // 사번
    private String userName;    // 사원명
    private String dept;        // 부서
    private String position;    // 직급

    private String roleCode;    // ADMIN / USER / MANAGER
    private String roleName;    // 시스템관리자 / 일반사용자 등
}
