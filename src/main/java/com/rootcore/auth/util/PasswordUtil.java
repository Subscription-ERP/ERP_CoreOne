package com.rootcore.auth.util;

import java.security.SecureRandom;

public class PasswordUtil {
	 private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
	    private static final int PASSWORD_LENGTH = 12;

	    public static String generateRandomPassword() {
	        SecureRandom random = new SecureRandom();
	        StringBuilder sb = new StringBuilder();

	        for (int i = 0; i < PASSWORD_LENGTH; i++) {
	            int idx = random.nextInt(CHARACTERS.length());
	            sb.append(CHARACTERS.charAt(idx));
	        }

	        return sb.toString();
	    }
}
