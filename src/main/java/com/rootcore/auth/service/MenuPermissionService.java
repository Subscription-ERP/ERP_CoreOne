package com.rootcore.auth.service;

import java.util.List;

import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.RoleVO;

public interface MenuPermissionService {

    // ==========================
    // USER 목록 조회 (LEFT)
    // ==========================
    List<MenuPermissionUserVO> getUserList(
            String companyCode,
            String userName,
            String dept,
            String position
    );

    // ==========================
    // ROLE 목록 조회 (CENTER)
    // ==========================
    List<RoleVO> getRoleList(String companyCode, String roleCode);

    // ==========================
    // ROLE → MENU 권한 조회/저장 (RIGHT)
    // ==========================
    List<RoleMenuAuthVO> getRoleMenuAuthList(String companyCode, String roleCode);

    int saveRoleMenuAuth(List<RoleMenuAuthVO> list);

    // ==========================
    // 로그인 사용자용 메뉴
    // ==========================
    List<RoleMenuAuthVO> getLoginMenuList(String companyCode, String roleCode);

    // ==========================
    // 선택 사용자 ROLE 일괄 변경
    // ==========================
    int updateUserRoleForUsers(String companyCode, String roleCode, List<String> userIds);
}
