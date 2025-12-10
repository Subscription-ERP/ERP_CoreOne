package com.rootcore.auth.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.rootcore.auth.mapper.LoginMapper;
import com.rootcore.auth.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginMapper loginMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        // 메서드명 변경됨(findByUserId)
        LoginVO loginVO = loginMapper.findByUserId(userId);

        if (loginVO == null) {
            log.warn("로그인 실패 - 사용자 없음 userId={}", userId);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return new SecurityUser(loginVO);
    }
}