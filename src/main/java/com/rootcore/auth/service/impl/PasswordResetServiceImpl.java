package com.rootcore.auth.service.impl;

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
    private final LoginMapper loginMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean sendResetLink(String userId, String email) {

        Map<String, Object> user = passwordResetMapper.findUserByIdAndEmail(userId, email);
        if (user == null) return false;

        String companyCode = (String) user.get("COMPANY_CODE");

        passwordResetMapper.invalidateOldToken(userId);

        String token = TokenUtil.generateSecureToken();

        Map<String, Object> param = Map.of(
                "companyCode", companyCode,
                "userId", userId,
                "token", token
        );

        passwordResetMapper.insertResetToken(param);

        String link = "http://localhost:8080/auth/password_reset?token=" + token;
        emailService.sendPasswordResetLink(email, link);

        return true;
    }

    @Override
    public boolean isValidToken(String token) {

        if (!TokenUtil.isValidTokenFormat(token)) {
            return false;
        }

        return passwordResetMapper.findValidToken(token) != null;
    }

    @Override
    public boolean resetPassword(String token, String newPassword, Map<String, String> error) {

        if (!TokenUtil.isValidTokenFormat(token)) {
            error.put("msg", "잘못된 요청입니다.");
            return false;
        }

        Map<String, Object> tokenInfo = passwordResetMapper.findValidToken(token);
        if (tokenInfo == null) {
            error.put("msg", "비밀번호 재설정 링크가 만료되었습니다.");
            return false;
        }

        String userId = (String) tokenInfo.get("USER_ID");
        String companyCode = (String) tokenInfo.get("COMPANY_CODE");

        if (!validatePasswordRules(newPassword, userId)) {
            error.put("msg", "비밀번호 규칙을 만족하지 않습니다.");
            return false;
        }

        String encoded = passwordEncoder.encode(newPassword);

        int updated = passwordResetMapper.updateUserPassword(companyCode, userId, encoded);
        if (updated <= 0) {
            error.put("msg", "비밀번호 변경에 실패했습니다.");
            return false;
        }

        loginMapper.unlockUserAccount(userId);
        passwordResetMapper.expireToken(token);

        log.info("[{}] 비밀번호 재설정 완료", userId);

        return true;
    }

    /** 비밀번호 규칙 */
    private boolean validatePasswordRules(String pw, String userId) {

        if (pw.length() < 8 || pw.length() > 20) return false;
        if (!pw.matches(".*[A-Za-z].*")) return false;
        if (!pw.matches(".*[0-9].*")) return false;
        if (!pw.matches(".*[!@#$%^&*()].*")) return false;
        if (pw.contains(" ")) return false;
        if (pw.matches(".*(.)\\1\\1.*")) return false;
        if (userId != null && pw.toLowerCase().contains(userId.toLowerCase())) return false;

        return true;
    }
}
