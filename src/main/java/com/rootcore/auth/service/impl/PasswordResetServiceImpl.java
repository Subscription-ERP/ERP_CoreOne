package com.rootcore.auth.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rootcore.auth.mapper.LoginMapper;
import com.rootcore.auth.mapper.PasswordResetMapper;
import com.rootcore.auth.service.EmailService;
import com.rootcore.auth.service.PasswordResetService;
import com.rootcore.auth.util.TokenUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetMapper passwordResetMapper;
    private final LoginMapper loginMapper;   // ★ 잠금 해제 기능을 위해 추가
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    //비밀번호 재설정 링크 이메일 발송 
    @Override
    public boolean sendResetLink(String userId, String email) {

        Map<String, Object> user = passwordResetMapper.findUserByIdAndEmail(userId, email);
        if (user == null) return false;

        String companyCode = (String) user.get("COMPANY_CODE");

        // 기존 토큰 만료 처리
        passwordResetMapper.invalidateOldToken(userId);

        // 새 토큰 발급
        String token = TokenUtil.generateSecureToken();

        Map<String, Object> param = new HashMap<>();
        param.put("companyCode", companyCode);
        param.put("userId", userId);
        param.put("token", token);

        passwordResetMapper.insertResetToken(param);

        // 이메일 발송
        String link = "http://localhost:8080/auth/password_reset?token=" + token;
        emailService.sendPasswordResetLink(email, link);

        return true;
    }

    /**
     * 토큰 유효성 검사
     */
    @Override
    public boolean isValidToken(String token) {

        if (!TokenUtil.isValidTokenFormat(token))
            return false;

        Map<String, Object> tokenInfo = passwordResetMapper.findValidToken(token);
        return tokenInfo != null;
    }

    /**
     * 비밀번호 규칙 검사
     */
    private boolean validatePasswordRules(String newPassword, String userId) {

        // 1) 길이 체크
        if (newPassword.length() < 8 || newPassword.length() > 20) {
            return false;
        }

        // 2) 영문 + 숫자 포함
        if (!newPassword.matches(".*[A-Za-z].*") ||
            !newPassword.matches(".*[0-9].*")) {
            return false;
        }

        // 3) 특수문자 포함
        if (!newPassword.matches(".*[!@#$%^&*()_+\\-=`~\\[\\]{};':\",.<>/?].*")) {
            return false;
        }

        // 4) 동일 문자 3회 이상 반복 금지
        if (newPassword.matches(".*(.)\\1\\1.*")) {  // aaa, ###, 111 등
            return false;
        }

        // 5) 사용자 ID 포함 금지
        if (userId != null && newPassword.toLowerCase().contains(userId.toLowerCase())) {
            return false;
        }

        // 6) 공백 금지
        if (newPassword.contains(" ")) {
            return false;
        }

        return true;
    }

    
    //비밀번호 재설정 실행 (★ 자동 unlock 포함)
    @Override
    public boolean resetPassword(String token, String newPassword) {

        if (!TokenUtil.isValidTokenFormat(token))
            return false;

        Map<String, Object> tokenInfo = passwordResetMapper.findValidToken(token);
        if (tokenInfo == null)
            return false;

        String userId = (String) tokenInfo.get("USER_ID");
        String companyCode = (String) tokenInfo.get("COMPANY_CODE");

        // 비밀번호 규칙 검사
        if (!validatePasswordRules(newPassword, userId)) {
            log.warn("비밀번호 규칙 불일치 - user={}, password={}", userId, newPassword);
            return false;
        }

        // 암호화
        String encoded = passwordEncoder.encode(newPassword);

        int updated = passwordResetMapper.updateUserPassword(companyCode, userId, encoded);

        if (updated <= 0) {
            log.warn("비밀번호 업데이트 실패 company={}, user={}", companyCode, userId);
            return false;
        }

        // ★ 비밀번호 재설정 성공 → 계정 잠금 해제
        loginMapper.unlockUserAccount(userId);

        log.info("[{}] 비밀번호 재설정 완료 → 계정 잠금 해제", userId);

        // 토큰 만료 처리
        passwordResetMapper.expireToken(token);

        return true;
    }
}
