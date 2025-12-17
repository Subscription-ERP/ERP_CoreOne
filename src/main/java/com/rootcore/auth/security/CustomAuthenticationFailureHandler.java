package com.rootcore.auth.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.rootcore.auth.mapper.LoginMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final LoginMapper loginMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String userId = request.getParameter("userId");

        log.warn("로그인 실패 원인: {}", exception.getMessage());

        // ✅ 1) 회사코드 불일치
        if (exception instanceof AuthenticationServiceException
                && "NO_COMPANY".equals(exception.getMessage())) {

            response.sendRedirect("/auth/login?error=noCompany");
            return;
        }

        // 2) 잠긴 계정
        if (exception instanceof LockedException) {
            response.sendRedirect("/auth/login?error=locked");
            return;
        }

        // 3) 아이디 / 비밀번호 오류
        if (exception instanceof BadCredentialsException && userId != null) {

            Integer failCount = loginMapper.getFailCount(userId);

            if (failCount == null) {
                response.sendRedirect("/auth/login?error=noUser");
                return;
            }

            if (failCount >= 5) {
                response.sendRedirect("/auth/login?error=locked");
                return;
            }

            // 실패 횟수 증가
            loginMapper.increaseFailCount(userId);
            int updatedFail = loginMapper.getFailCount(userId);

            log.warn("[{}] 로그인 실패 {}회", userId, updatedFail);

            if (updatedFail >= 5) {
                loginMapper.lockUserAccount(userId);
                response.sendRedirect("/auth/login?error=locked");
                return;
            }

            int remain = 5 - updatedFail;
            response.sendRedirect("/auth/login?error=wrong&remain=" + remain);
            return;
        }

        // 4) 기타 오류
        response.sendRedirect("/auth/login?error=fail");
    }
}
