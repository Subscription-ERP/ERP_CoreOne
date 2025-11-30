package com.rootcore.auth.security;

import com.rootcore.auth.mapper.LoginMapper;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final LoginMapper loginMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {

        String userId = request.getParameter("userId");

        // 실패 횟수 초기화
        loginMapper.resetFailCount(userId);

        // last login 업데이트
        loginMapper.updateLastLogin(userId);

        // remember-id 쿠키 저장
        boolean remember = "on".equals(request.getParameter("rememberId"));

        if (remember) {
            Cookie cookie = new Cookie("rememberId", userId);
            cookie.setMaxAge(60 * 60 * 24 * 30); // 30일
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        try {
            response.sendRedirect("/"); // 로그인 성공 → 메인화면 이동
        } catch (Exception e) {
            log.error("로그인 성공 redirect 오류", e);
        }
    }
}
