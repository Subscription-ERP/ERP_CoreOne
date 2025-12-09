package com.rootcore.auth.web;

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


    // ROLE 목록 조회

    @GetMapping("/role")
    public List<RoleVO> getRoleList(
            @RequestParam(required = false) String roleCode,
            HttpSession session
    ) {

        String company = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        if (company == null || company.isEmpty()) {
            company = "0000";   // ✅ 개발용 기본 회사코드
        }

        return service.getRoleList(company, roleCode);
    }


    // ROLE → MENU 권한 조회

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


    // ROLE → MENU 권한 저장 (DB 반영)

    @PostMapping("/role-menu/save")
    public int saveRoleMenuAuth(
            @RequestBody List<RoleMenuAuthVO> list,
            HttpSession session
    ) {

        // 빈 리스트 방어
        if (list == null || list.isEmpty()) {
            return 0;
        }

        // 회사코드 처리
        String company = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        if (company == null || company.isEmpty()) {
            company = "0000";
        }
        final String companyCode = company;

        // 로그인 사용자
        String loginUser = (String) session.getAttribute("LOGIN_USER_ID");
        if (loginUser == null || loginUser.isEmpty()) {
            loginUser = "SYSTEM";
        }
        final String loginUserId = loginUser;

        // ADMIN 1차 보호 (이중 잠금)
        String roleCode = list.get(0).getRoleCode();
        if ("ADMIN".equals(roleCode)) {
            return 1;   // 저장 안 해도 이미 전권자
        }

        // 회사코드 + 생성자 + 수정자 + 필수값 방어
        for (RoleMenuAuthVO vo : list) {

            if (vo.getRoleCode() == null || vo.getMenuCode() == null) {
                continue;   // 깨진 데이터는 저장 안 함
            }

            vo.setCompanyCode(companyCode);
            vo.setCreatedBy(loginUserId);
            vo.setUpdatedBy(loginUserId);
        }

        return service.saveRoleMenuAuth(list);
    }
}
