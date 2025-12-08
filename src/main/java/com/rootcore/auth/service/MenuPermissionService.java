package com.rootcore.auth.service;

import java.util.List;

import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.UserAuthVO;

public interface MenuPermissionService {

    // ✅ USER 목록 조회
    List<UserAuthVO> getUserList(
            String companyCode,
            String userName,
            String dept,
            String position
    );

    // ✅ ROLE → MENU 권한 조회
    List<RoleMenuAuthVO> getRoleMenuAuthList(
            String companyCode,
            String roleCode
    );

    // ✅ ROLE → MENU 권한 저장
    int saveRoleMenuAuth(List<RoleMenuAuthVO> list);
}
