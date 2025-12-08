package com.rootcore.auth.web;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.UserAuthVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/menu_permission/api")
public class MenuPermissionController {

    private final MenuPermissionService service;

    // ✅ 1) USER 목록 조회
    @GetMapping("/user")
    public List<UserAuthVO> getUserList(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) String position,
            HttpSession session
    ) {
        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");

        // ✅ 세션 없을 경우 안전 처리 (개발용)
        if (companyCode == null || companyCode.isEmpty()) {
            companyCode = "ROOT";   // 네 테이블 기본 회사코드
        }

        return service.getUserList(companyCode, userName, dept, position);
    }


    // ✅ 2) ROLE → MENU 권한 조회
    @GetMapping("/role-menu")
    public List<RoleMenuAuthVO> getRoleMenuAuthList(
            @RequestParam String roleCode,
            HttpSession session
    ) {
        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        return service.getRoleMenuAuthList(companyCode, roleCode);
    }

    // ✅ 3) ROLE → MENU 권한 저장
    @PostMapping("/role-menu/save")
    public int saveRoleMenuAuth(
            @RequestBody List<RoleMenuAuthVO> list,
            HttpSession session
    ) {
        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        list.forEach(vo -> vo.setCompanyCode(companyCode));
        return service.saveRoleMenuAuth(list);
    }
}
