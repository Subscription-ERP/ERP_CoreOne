package com.rootcore.auth.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.RoleMenuAuthVO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // Mapper 제거 → Service  사용
    private final MenuPermissionService menuPermissionService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        // 로그인한 사용자 ID
        String userId = authentication.getName();

        // 회사코드 / ROLE 코드 (지금은 고정, 나중에 DB에서 조회)
        String companyCode = "0000";
        String roleCode = "ADMIN";   // 👉 추후: USER / MANAGER / ADMIN DB값 연동

        // 운영급 ROLE 권한 조회 (Service가 정책 자동 적용)
        List<RoleMenuAuthVO> roleMenuAuthList =
                menuPermissionService.getRoleMenuAuthList(companyCode, roleCode);

        // 세션 저장 (이게 “권한의 기준값”이 된다)
        session.setAttribute("LOGIN_USER_ID", userId);
        session.setAttribute("LOGIN_COMPANY_CODE", companyCode);
        session.setAttribute("LOGIN_ROLE_CODE", roleCode);
        session.setAttribute("LOGIN_MENU_AUTH", roleMenuAuthList);

        // 로그인 성공 후 메인으로 이동
        response.sendRedirect("/");
    }
}
