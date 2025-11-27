package com.rootcore.auth.service.impl;

import com.rootcore.auth.mapper.PasswordResetMapper;
import com.rootcore.auth.service.EmailService;
import com.rootcore.auth.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetMapper passwordResetMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder; // Security Config에 Bean 등록되어 있어야 함

    /**
     * 비밀번호 재설정 링크 발송
     */
    @Override
    public boolean sendResetLink(String userId, String email) {

        // 1) USER 존재 여부 확인
        Map<String, Object> user = passwordResetMapper.findUserByIdAndEmail(userId, email);
        if (user == null) {
            log.warn("비밀번호 재설정 요청 실패 - 사용자 정보 불일치 userId={}, email={}", userId, email);
            return false;
        }

        String companyCode = (String) user.getOrDefault("COMPANY_CODE", "ROOT");

        // 2) 기존 토큰 무효화
        passwordResetMapper.invalidateOldToken(userId);

        // 3) 새 토큰 생성 및 저장
        String token = UUID.randomUUID().toString();

        Map<String, Object> param = new HashMap<>();
        param.put("companyCode", companyCode);
        param.put("userId", userId);
        param.put("token", token);

        passwordResetMapper.insertResetToken(param);

        // 4) 이메일 발송
        String resetLink = "http://localhost:8080/auth/password_reset?token=" + token;
        emailService.sendPasswordResetLink(email, resetLink);

        log.info("비밀번호 재설정 링크 발송 완료 -> userId={}, email={}, token={}", userId, email, token);
        return true;
    }

    /**
     * 토큰 유효 여부 확인
     */
    @Override
    public boolean isValidToken(String token) {
        if (token == null || token.isBlank()) return false;
        Map<String, Object> tokenInfo = passwordResetMapper.findValidToken(token);
        return tokenInfo != null;
    }

    /**
     * 실제 비밀번호 변경
     */
    @Override
    public boolean resetPassword(String token, String newPassword) {
        // 1) 토큰 유효 여부 + 사용자 정보 조회
        Map<String, Object> tokenInfo = passwordResetMapper.findValidToken(token);
        if (tokenInfo == null) {
            log.warn("비밀번호 재설정 실패 - 유효하지 않은 토큰 token={}", token);
            return false;
        }

        String userId = (String) tokenInfo.get("USER_ID");

        // 2) 새 비밀번호 암호화
        String encodedPw = passwordEncoder.encode(newPassword);

        // 3) 비밀번호 변경
        int updated = passwordResetMapper.updateUserPassword(userId, encodedPw);
        if (updated <= 0) {
            log.warn("비밀번호 재설정 실패 - 비밀번호 업데이트 실패 userId={}", userId);
            return false;
        }

        // 4) 토큰 사용 완료 처리
        passwordResetMapper.expireToken(token);

        log.info("비밀번호 재설정 완료 userId={}", userId);
        return true;
    }
}
