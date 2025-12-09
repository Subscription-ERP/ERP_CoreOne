package com.rootcore.sb.client;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.rootcore.sb.vo.TossBillingConfirmRequestVO;
import com.rootcore.sb.vo.TossConfirmRequestVO;
import com.rootcore.sb.vo.TossConfirmResponseVO;

import reactor.core.publisher.Mono;

//토스 결제승인 API를 호출하는 WebClient 기반 클래스
//webclient가 HTTP 요청을 보내면 TosspaymentClient에서
//webclient를 호출해서 토스서버와 통신 
@Component
public class TossPaymentClient {

	// webclient 호출해서 사용
	private final WebClient tossWebClient;

	// 토스 webclient 주입받는 생성자
	public TossPaymentClient(WebClient tossWebClient) {
		this.tossWebClient = tossWebClient;
	}

	// 결제 승인용 데이터를 받고 토스 api로 승인 요청을 전송
	public TossConfirmResponseVO confirmPayment(TossConfirmRequestVO requestVO) {

		// WebClient를 사용해 토스 결제 승인 API 호출
		Mono<TossConfirmResponseVO> mono = tossWebClient.post().uri("/v1/payments/confirm") // Toss Confirm API 엔드포인트
				.bodyValue(requestVO) // HTTP Body에 JSON으로 들어갈 데이터
				.retrieve() // HTTP 응답 가져오기
				.bodyToMono(TossConfirmResponseVO.class);
		// Mono는 Reactor의 비동기/리액티브 타입입니다.
		// Spring MVC 환경이라면 block()으로 동기적으로 결과를 받아도 무방 (비즈니스 요구에 따라 조정)
		return mono.block();
	}

	// 정기결제(자동결제) 승인 API 호출
	public TossConfirmResponseVO confirmBillingPayment(TossBillingConfirmRequestVO req) {

		Map<String, Object> body = Map.of("amount", req.getAmount(), "orderId", req.getOrderId());

		Mono<TossConfirmResponseVO> mono = tossWebClient.post().uri("/v1/billing/{billingKey}", req.getBillingKey())
				.bodyValue(body).retrieve().bodyToMono(TossConfirmResponseVO.class);

		return mono.block();
	}
	   // 3) 빌링키 발급
    public String issueBillingKey(String authKey, String customerKey) {

        Map<String, String> body = Map.of(
                "authKey", authKey,
                "customerKey", customerKey
        );

        Mono<Map> mono = tossWebClient.post()
                .uri("/v1/billing/authorizations/issue")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class);

        Map<String, Object> response = mono.block();
        return (String) response.get("billingKey");
    }
}