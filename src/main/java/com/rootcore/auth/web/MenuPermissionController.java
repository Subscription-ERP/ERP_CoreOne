package com.rootcore.auth.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.RoleVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/menu_permission/api")
public class MenuPermissionController {

    private final MenuPermissionService service;

    // ✅ ROLE 목록 조회 (JS Grid용 camelCase 강제 보정)
    @GetMapping("/role")
    public List<RoleVO> getRoleList(
            @RequestParam(required = false) String roleCode,
            HttpSession session
    ) {

        String company = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        if (company == null || company.isEmpty()) {
            company = "0000";   // ✅ 개발용 기본값
        }

        List<RoleVO> rawList = service.getRoleList(company, roleCode);

        // ✅ 여기서 JS가 100% 인식 가능하도록 camelCase 강제 보정
        List<RoleVO> safeList = new ArrayList<>();
        for (RoleVO vo : rawList) {
            RoleVO safe = new RoleVO();
            safe.setRoleCode(vo.getRoleCode());   // ← 반드시 camelCase
            safe.setRoleName(vo.getRoleName());
            safeList.add(safe);
        }

        return safeList;
    }

    // ✅ ROLE → MENU 권한 조회
    @GetMapping("/role-menu")
    public List<RoleMenuAuthVO> getRoleMenuAuthList(
            @RequestParam String roleCode,
            HttpSession session
    ) {

        String company = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        if (company == null || company.isEmpty()) {
            company = "0000";
        }

        return service.getRoleMenuAuthList(company, roleCode);
    }

    // ✅ ROLE → MENU 권한 저장
    @PostMapping("/role-menu/save")
    public int saveRoleMenuAuth(
            @RequestBody List<RoleMenuAuthVO> list,
            HttpSession session
    ) {

        if (list == null || list.isEmpty()) {
            return 0;
        }

        String company = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        if (company == null || company.isEmpty()) {
            company = "0000";
        }

        String loginUser = (String) session.getAttribute("LOGIN_USER_ID");
        if (loginUser == null || loginUser.isEmpty()) {
            loginUser = "SYSTEM";
        }

        String roleCode = list.get(0).getRoleCode();

        // ✅ ADMIN은 절대 DB 저장 금지
        if ("ADMIN".equals(roleCode)) {
            return 1;
        }

        for (RoleMenuAuthVO vo : list) {

            if (vo.getRoleCode() == null || vo.getMenuCode() == null) {
                continue;
            }

            vo.setCompanyCode(company);
            vo.setCreatedBy(loginUser);
            vo.setUpdatedBy(loginUser);
        }

        return service.saveRoleMenuAuth(list);
    }
}