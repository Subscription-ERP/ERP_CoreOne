package com.rootcore.auth.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean sendResetLink(String userId, String email) {

        Map<String, Object> user = passwordResetMapper.findUserByIdAndEmail(userId, email);
        if (user == null) {
            return false;
        }

        String companyCode = (String) user.get("COMPANY_CODE");

        // 기존 토큰 무효화
        passwordResetMapper.invalidateOldToken(userId);

        // 새로운 토큰 생성
        String token = TokenUtil.generateSecureToken();

        // 저장
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

    @Override
    public boolean isValidToken(String token) {

        if (!TokenUtil.isValidTokenFormat(token)) return false;

        Map<String, Object> tokenInfo = passwordResetMapper.findValidToken(token);
        return tokenInfo != null;
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {

        if (!TokenUtil.isValidTokenFormat(token))
            return false;

        Map<String, Object> tokenInfo = passwordResetMapper.findValidToken(token);
        if (tokenInfo == null)
            return false;

        String userId = (String) tokenInfo.get("USER_ID");
        String companyCode = (String) tokenInfo.get("COMPANY_CODE");

        String encoded = passwordEncoder.encode(newPassword);

        // UPDATE COMPANY_CODE 포함
        int updated = passwordResetMapper.updateUserPassword(companyCode, userId, encoded);

        if (updated <= 0) {
            log.warn("비밀번호 업데이트 실패 company={}, user={}", companyCode, userId);
            return false;
        }

        // 토큰 만료 처리
        passwordResetMapper.expireToken(token);

        return true;
    }
}
