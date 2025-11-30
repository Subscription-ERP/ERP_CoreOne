package com.rootcore.auth.util;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    // 32바이트(256bit) 강력 난수 토큰 생성
    public static String generateSecureToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    // 토큰 형식 자체를 검증 (보안 위협 차단)
    public static boolean isValidTokenFormat(String token) {
        if (token == null) return false;
        return token.matches("^[A-Za-z0-9_-]{30,100}$");
    }
}
