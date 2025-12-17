package com.rootcore.auth.security;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.rootcore.auth.mapper.LoginMapper;
import com.rootcore.auth.vo.LoginVO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginMapper loginMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        // 🔹 현재 요청 객체 가져오기
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs == null) {
            throw new AuthenticationServiceException("REQUEST_NOT_FOUND");
        }

        HttpServletRequest request = attrs.getRequest();

        // 🔹 로그인 폼에서 넘어온 회사코드
        String companyCode = request.getParameter("companyCode");

        log.info("로그인 시도 userId={}, companyCode={}", userId, companyCode);

        // 🔹 회사코드 + 아이디로 사용자 조회
        LoginVO loginVO =
                loginMapper.findByUserIdAndCompany(companyCode, userId);

        // ❌ 회사코드 불일치 or 사용자 없음
        if (loginVO == null) {
            log.warn("로그인 실패 - 회사코드 불일치 userId={}, companyCode={}", userId, companyCode);
            throw new AuthenticationServiceException("NO_COMPANY");
        }

        return new SecurityUser(loginVO);
    }
}
