package com.rootcore.sb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration	//Spring에게“여기 안에 있는 @Bean 메서드를 등록해줘”
//라고 알려주는 설정 클래스입니다.

//토스 서버로 요청을 보내는 메소드
public class tossPaymentsWebClientConfig {
	
	@Bean	//Spring 컨테이너에 WebClient 객체를 등록합니다.
    //이 Bean은 어디서든 주입받아 사용할 수 있습니다.
    public WebClient tossPaymentsWebClient(
            @Value("${toss.payments.secret-key}") String secretKey
    ) {
    	//Toss Payments는 API 요청 시 Basic Auth 방식의 인증을 사용
        String credential = secretKey + ":";
        String encoded = Base64.getEncoder()
                .encodeToString(credential.getBytes(StandardCharsets.UTF_8));
        String basicAuth = "Basic " + encoded;

        return WebClient.builder()
                .baseUrl("https://api.tosspayments.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, basicAuth)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }
}

