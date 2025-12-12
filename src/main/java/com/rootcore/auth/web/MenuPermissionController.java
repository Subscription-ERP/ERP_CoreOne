package com.rootcore.auth.web;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.RoleVO;
import com.rootcore.auth.vo.UserRoleAssignRequest;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/menu_permission/api")
public class MenuPermissionController {

    private final MenuPermissionService service;

    // ================================
    // 1) 사용자 목록 조회 (LEFT GRID)
    // ================================
    @GetMapping("/user")
    public List<MenuPermissionUserVO> getUserList(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) String position,
            HttpSession session
    ) {
        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        if (companyCode == null || companyCode.isEmpty()) {
            companyCode = "0000";  // 개발용 기본값
        }

        return service.getUserList(companyCode, userName, dept, position);
    }

    // ================================
    // 2) ROLE 목록 조회 (CENTER GRID)
    // ================================
    @GetMapping("/role")
    public List<RoleVO> getRoleList(
            @RequestParam(required = false) String roleCode,
            HttpSession session
    ) {
        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        if (companyCode == null || companyCode.isEmpty()) {
            companyCode = "0000";
        }

        return service.getRoleList(companyCode, roleCode);
    }

    // ================================
    // 3) ROLE → MENU 권한 조회 (RIGHT GRID)
    // ================================
    @GetMapping("/role-menu")
    public List<RoleMenuAuthVO> getRoleMenuAuthList(
            @RequestParam String roleCode,
            HttpSession session
    ) {
        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        if (companyCode == null || companyCode.isEmpty()) {
            companyCode = "0000";
        }

        return service.getRoleMenuAuthList(companyCode, roleCode);
    }

    // ================================
    // 4) ROLE → MENU 권한 저장
    // ================================
    @PostMapping("/role-menu/save")
    public int saveRoleMenuAuth(
            @RequestBody List<RoleMenuAuthVO> list,
            HttpSession session
    ) {

        if (list == null || list.isEmpty()) {
            return 0;
        }

        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        if (companyCode == null || companyCode.isEmpty()) {
            companyCode = "0000";
        }

        String loginUser = (String) session.getAttribute("LOGIN_USER_ID");
        if (loginUser == null || loginUser.isEmpty()) {
            loginUser = "SYSTEM";
        }

        // 공통 세팅
        for (RoleMenuAuthVO vo : list) {
            vo.setCompanyCode(companyCode);
            vo.setCreatedBy(loginUser);
            vo.setUpdatedBy(loginUser);
        }

        return service.saveRoleMenuAuth(list);
    }

    // ================================
    // 5) 선택 사용자 → ROLE 일괄 부여
    // ================================
    @PostMapping("/user/setRole")
    public int setUserRole(
            @RequestBody UserRoleAssignRequest request,
            HttpSession session
    ) {

        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        if (companyCode == null || companyCode.isEmpty()) {
            companyCode = "0000";
        }

        if (request.getUserIds() == null || request.getUserIds().isEmpty()) {
            return 0;
        }

        return service.updateUserRoleForUsers(companyCode, request.getRoleCode(), request.getUserIds());
    }
}
