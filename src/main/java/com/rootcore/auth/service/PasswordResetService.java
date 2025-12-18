package com.rootcore.auth.service;

import java.util.Map;

public interface PasswordResetService {

    boolean sendResetLink(String userId, String email);

    boolean isValidToken(String token);

    /**
     * @param error 실패 사유 전달용
     */
    boolean resetPassword(String token, String newPassword, Map<String, String> error);
}
