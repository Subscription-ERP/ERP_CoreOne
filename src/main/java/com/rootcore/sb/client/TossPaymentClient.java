package com.rootcore.sb.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.rootcore.sb.dto.TossConfirmRequestDto;
import com.rootcore.sb.dto.TossConfirmResponseDto;

import reactor.core.publisher.Mono;

@Component
public class TossPaymentClient {

    private final WebClient tossWebClient;

    public TossPaymentClient(WebClient tossWebClient) {
        this.tossWebClient = tossWebClient;
    }

    public TossConfirmResponseDto confirmPayment(TossConfirmRequestDto requestDto) {

        // WebClient를 사용해 토스 결제 승인 API 호출
        Mono<TossConfirmResponseDto> mono = tossWebClient.post()
                .uri("/v1/payments/confirm")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(TossConfirmResponseDto.class);

        // Spring MVC 환경이라면 block()으로 동기적으로 결과를 받아도 무방 (비즈니스 요구에 따라 조정)
        return mono.block();
    }
}