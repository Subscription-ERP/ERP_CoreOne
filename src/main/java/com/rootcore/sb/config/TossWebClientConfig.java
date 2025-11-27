package com.rootcore.sb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class TossWebClientConfig {

    @Bean
    public WebClient tossWebClient(
            @Value("${toss.api.base-url}") String baseUrl,
            @Value("${toss.api.secret-key}") String secretKey
    ) {
        String credential = secretKey + ":";
        String encoded = Base64.getEncoder()
                .encodeToString(credential.getBytes(StandardCharsets.UTF_8));
        String basicAuth = "Basic " + encoded;

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, basicAuth)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }
}