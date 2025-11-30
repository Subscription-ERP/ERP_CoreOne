package com.rootcore.auth.service.impl;

import com.rootcore.auth.mapper.LoginMapper;
import com.rootcore.auth.service.LoginService;
import com.rootcore.auth.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;

    private static final int MAX_FAIL_COUNT = 5;

    @Override
    public LoginVO login(String userId, String rawPassword) {

        LoginVO user = loginMapper.findByUserId(userId);

        // 1) 사용자 없음
        if (user == null) {
            log.warn("로그인 실패 - 존재하지 않는 ID: {}", userId);
            return failResult("존재하지 않는 사용자입니다.");
        }

        // 2) 계정 잠금 상태
        if ("LOCKED".equalsIgnoreCase(user.getStatus())) {
            log.warn("로그인 실패 - 계정 잠김: {}", userId);
            return failResult("계정이 잠겨 있습니다. 관리자에게 문의하세요.");
        }

        // 3) 비밀번호 검증
        boolean passwordMatch = passwordEncoder.matches(rawPassword, user.getDbPassword());

        if (!passwordMatch) {

            // 실패횟수 증가
            loginMapper.increaseFailCount(userId);

            int newFailCount = loginMapper.getFailCount(userId);
            log.warn("[{}] 로그인 실패 {}회", userId, newFailCount);

            // 5회 이상 → 계정 잠금
            if (newFailCount >= MAX_FAIL_COUNT) {
                loginMapper.lockUserAccount(userId);
                return failResult("5회 이상 실패하여 계정이 잠겼습니다.");
            }

            return failResult("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        // 4) 로그인 성공 → 실패횟수 초기화, 마지막 로그인 시간 업데이트
        loginMapper.resetFailCountAndLastLogin(userId);

        user.setLoginSuccess(true);
        user.setMessage("로그인 성공");
        return user;
    }

    private LoginVO failResult(String message) {
        LoginVO vo = new LoginVO();
        vo.setLoginSuccess(false);
        vo.setMessage(message);
        return vo;
    }
}
