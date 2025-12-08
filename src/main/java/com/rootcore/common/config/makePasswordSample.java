package com.rootcore.common.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class makePasswordSample {

    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "1234";  // 여기다가 원하는 비밀번호
        String encoded = encoder.encode(rawPassword);

        System.out.println("원문 비밀번호: " + rawPassword);
        System.out.println("암호화 결과:  " + encoded);
    }
}


// 원문 비밀번호: 1234
// 암호화 결과:  $2a$10$9D8SJFi7o81tBYkZpZF.YOxVhkgnu.uUpPr6FVhCny8JpeEqI/hwu