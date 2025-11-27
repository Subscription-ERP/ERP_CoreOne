package com.rootcore.auth.service;

public interface PasswordResetService {

    // 1) 비밀번호 재설정 링크 발송
    boolean sendResetLink(String userId, String email);

    // 2) 토큰 유효 여부 확인 (화면 진입용)
    boolean isValidToken(String token);

    // 3) 실제 비밀번호 변경
    boolean resetPassword(String token, String newPassword);
}
