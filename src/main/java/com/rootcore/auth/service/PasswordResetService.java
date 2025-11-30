package com.rootcore.auth.service;

public interface PasswordResetService {

    boolean sendResetLink(String userId, String email);

    boolean isValidToken(String token);

    boolean resetPassword(String token, String newPassword);
}
