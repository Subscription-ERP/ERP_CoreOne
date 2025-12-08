package com.rootcore.auth.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        // ✅ 로그인한 사용자 ID
        String userId = authentication.getName();

        // ✅ 회사코드 → DB 기준과 반드시 일치시켜야 함
        String companyCode = "0000";

        session.setAttribute("LOGIN_USER_ID", userId);
        session.setAttribute("LOGIN_COMPANY_CODE", companyCode);

        response.sendRedirect("/");
    }
}

