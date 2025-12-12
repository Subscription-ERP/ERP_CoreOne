package com.rootcore.auth.vo;

import lombok.Data;

/**
 * 메뉴권한관리 화면 - 사용자 목록 GRID 전용 VO
 */
@Data
public class MenuPermissionUserVO {

    private String companyCode;

    private String userId;      // 사번
    private String userName;    // 사원명
    private String dept;        // 부서
    private String position;    // 직급

    private String roleCode;    // ADMIN / MANAGER / USER ...
    private String roleName;    // ROLE 설명
}
