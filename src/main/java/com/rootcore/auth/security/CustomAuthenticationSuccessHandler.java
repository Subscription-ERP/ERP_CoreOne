package com.rootcore.auth.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.rootcore.auth.mapper.MenuPermissionMapper;
import com.rootcore.auth.vo.MenuAuthSaveVO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MenuPermissionMapper menuPermissionMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        String userId = authentication.getName();

        // 🔹 나중에 회사코드 선택 기능 만들면 여기서 실제 값으로 변경
        String companyCode = "0000";

        // 1) DB에서 권한 조회 (선택 사항 – 기존에 쓰던 로직 유지)
        List<MenuAuthSaveVO> authList =
                menuPermissionMapper.selectUserMenuAuth(companyCode, userId);

        // 2) 세션 저장
        session.setAttribute("LOGIN_USER_ID", userId);
        session.setAttribute("LOGIN_COMPANY_CODE", companyCode);
        session.setAttribute("USER_MENU_AUTH", authList);

        // 3) 메인 페이지로 이동
        response.sendRedirect("/");
    }
}
