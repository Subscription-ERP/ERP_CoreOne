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

        
        // 1) 존재하지 않는 사용자
        if (exception instanceof AuthenticationServiceException) {
            response.sendRedirect("/auth/login?error=noUser");
            return;
        }

        // 2) 잠긴 계정

        if (exception instanceof LockedException) {
            response.sendRedirect("/auth/login?error=locked");
            return;
        }

        
        // 3) 아이디/비밀번호 틀림
        if (exception instanceof BadCredentialsException && userId != null) {

            Integer failCount = loginMapper.getFailCount(userId);

            // DB에 없는 사용자 → 위에서 잡히지만 혹시 null이 온 경우 대비
            if (failCount == null) {
                response.sendRedirect("/auth/login?error=noUser");
                return;
            }

            // 이미 잠긴 상태면 lock 메시지
            if (failCount >= 5) {
                response.sendRedirect("/auth/login?error=locked");
                return;
            }

            // 실패 횟수 증가
            loginMapper.increaseFailCount(userId);
            int updatedFail = loginMapper.getFailCount(userId);

            log.warn("[{}] 로그인 실패 {}회", userId, updatedFail);

            // 5회 → 계정 잠금
            if (updatedFail >= 5) {
                loginMapper.lockUserAccount(userId);
                response.sendRedirect("/auth/login?error=locked");
                return;
            }

            // 남은 횟수 전달
            int remain = 5 - updatedFail;
            response.sendRedirect("/auth/login?error=wrong&remain=" + remain);
            return;
        }

        
        // 4) 그 외 모든 로그인 에러
        response.sendRedirect("/auth/login?error=fail");
    }
}
