package com.rootcore.auth.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
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

        // 로그인 ID
        String userId = authentication.getName();

        // DB에서 회사코드 + ROLE_CODE 조회
        LoginUserVO loginUser = loginMapper.selectLoginUser(userId);

        if (loginUser == null) {
            response.sendRedirect("/auth/login?error");
            return;
        }

        String companyCode = loginUser.getCompanyCode();
        String roleCode = loginUser.getRoleCode();

        // ROLE_ 제거 및 대문자 변환
        roleCode = roleCode.replace("ROLE_", "").toUpperCase();

        // ⭐⭐⭐ 관리자 화면용 전체 권한 조회는 이제 쓰지 않는다
        // List<RoleMenuAuthVO> roleMenuAuthList =
        //        menuPermissionService.getRoleMenuAuthList(companyCode, roleCode);

        // ⭐⭐⭐ 로그인 사용자 사이드바 전용 메뉴만 조회한다 (READ_YN = 'Y')
        List<RoleMenuAuthVO> loginMenuList =
                menuPermissionService.getLoginMenuList(companyCode, roleCode);

        // 세션 저장
        session.setAttribute("LOGIN_USER_ID", userId);
        session.setAttribute("LOGIN_COMPANY_CODE", companyCode);
        session.setAttribute("LOGIN_ROLE_CODE", roleCode);
        
        // ⭐⭐⭐ 이제 사이드 메뉴는 이것만 사용해야 한다
        session.setAttribute("LOGIN_MENU_AUTH", loginMenuList);

        // 로그인 성공 처리
        loginMapper.resetFailCountAndLastLogin(userId);
        loginMapper.unlockUserAccount(userId);

        // 디버그
        System.out.println("LOGIN ID = " + userId);
        System.out.println("LOGIN ROLE_CODE = " + roleCode);
        System.out.println("LOGIN_MENU_AUTH.size = " + loginMenuList.size());

        response.sendRedirect("/");
    }
}