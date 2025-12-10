//package com.rootcore.sb.crypto;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//@Component
//public class BillingKeyCrypto {
//
//    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
//
//    @Value("${billing.secret-key}")
//    private String secretKeyString;
//
//    private SecretKeySpec secretKeySpec;
//
//    @PostConstruct
//    public void init() {
//        byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
//        this.secretKeySpec = new SecretKeySpec(keyBytes, "AES"); // 16/24/32바이트 가정
//    }
//
//    // 🔐 평문 -> 암호문(Base64)
//    public String encrypt(String plainText) {
//        try {
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
//            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
//            return Base64.getEncoder().encodeToString(encrypted);
//        } catch (Exception e) {
//            throw new RuntimeException("BillingKey 암호화 실패", e);
//        }
//    }
//
//    // 🔓 암호문(Base64) -> 평문
//    public String decrypt(String encryptedText) {
//        try {
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
//            byte[] decoded = Base64.getDecoder().decode(encryptedText);
//            byte[] decrypted = cipher.doFinal(decoded);
//            return new String(decrypted, StandardCharsets.UTF_8);
//        } catch (Exception e) {
//            throw new RuntimeException("BillingKey 복호화 실패", e);
//        }
//    }
//}
