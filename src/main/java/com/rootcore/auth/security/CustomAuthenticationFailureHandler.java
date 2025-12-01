package com.rootcore.auth.security;

import java.io.IOException;
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
                                        org.springframework.security.core.AuthenticationException exception)
            throws IOException, ServletException {

        String userId = request.getParameter("userId");

        if (userId != null) {
            loginMapper.increaseFailCount(userId);

            int failCount = loginMapper.getFailCount(userId);
            log.warn("[{}] 로그인 실패 {}회", userId, failCount);

            if (failCount >= 5) {
                loginMapper.lockUserAccount(userId);
                response.sendRedirect("/auth/login?error=locked");
                return;
            }
        }

        response.sendRedirect("/auth/login?error=true");
    }
}
