package com.rootcore.auth.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.rootcore.auth.mapper.LoginMapper;
import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.LoginUserVO;
import com.rootcore.auth.vo.RoleMenuAuthVO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MenuPermissionService menuPermissionService;
    private final LoginMapper loginMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        //  로그인 ID
        String userId = authentication.getName();

        //  DB에서 회사코드 + ROLE_CODE 조회
        LoginUserVO loginUser = loginMapper.selectLoginUser(userId);

        if (loginUser == null) {
            response.sendRedirect("/auth/login?error");
            return;
        }

        String companyCode = loginUser.getCompanyCode();
        String roleCode    = loginUser.getRoleCode();   // ✅ 이제 여기서 정확히 ROLE_CODE 받는다

        //  혹시 소문자나 ROLE_ADMIN 형태면 정리
        roleCode = roleCode.replace("ROLE_", "").toUpperCase();

        //  역할 기준 메뉴 권한 조회
        List<RoleMenuAuthVO> roleMenuAuthList =
                menuPermissionService.getRoleMenuAuthList(companyCode, roleCode);

        //  세션 저장
        session.setAttribute("LOGIN_USER_ID", userId);
        session.setAttribute("LOGIN_COMPANY_CODE", companyCode);
        session.setAttribute("LOGIN_ROLE_CODE", roleCode);   // ✅ ADMIN / MANAGER / USER
        session.setAttribute("LOGIN_MENU_AUTH", roleMenuAuthList);

        //  로그인 성공 처리
        loginMapper.resetFailCountAndLastLogin(userId);
        loginMapper.unlockUserAccount(userId);

        //  디버그 로그 (이거 꼭 한 번 봐라)
        System.out.println("✅ LOGIN ID = " + userId);
        System.out.println("✅ LOGIN ROLE_CODE = " + roleCode);

        response.sendRedirect("/");
    }
}
