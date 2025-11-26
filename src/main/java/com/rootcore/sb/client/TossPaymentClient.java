package com.rootcore.sb.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.rootcore.sb.vo.TossConfirmRequest;
import com.rootcore.sb.vo.TossConfirmResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
//토스에게 결제승인 요청하는 클래스
@Component
@RequiredArgsConstructor
public class TossPaymentClient {

//    private final WebClient tossWebClient;
//
//    public TossConfirmResponse confirmPayment(TossConfirmRequest request) {
//        return tossWebClient.post()
//                .uri("/payments/confirm")
//                .bodyValue(request)
//                .retrieve()
//                .bodyToMono(TossConfirmResponse.class)
//                .onErrorResume(e -> Mono.error(e))
//                .block(); // 동기 방식 사용
//    }
}
