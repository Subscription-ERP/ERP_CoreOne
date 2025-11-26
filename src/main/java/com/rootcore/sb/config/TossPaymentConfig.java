//package com.rootcore.sb.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//@Configuration
//public class TossPaymentConfig {
//
//    @Value("${toss.secret-key}")
//    private String secretKey; // application.yml 에 설정해두기
//
//    @Bean
//    public WebClient tossWebClient() {
//        String encodedAuth = Base64.getEncoder()
//                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
//
//        return WebClient.builder()
//                .baseUrl("https://api.tosspayments.com/v1")
//                .defaultHeader("Authorization", "Basic " + encodedAuth)
//                .build();
//    }
//}