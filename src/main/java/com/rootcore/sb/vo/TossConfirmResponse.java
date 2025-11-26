package com.rootcore.sb.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//토스가 보내주는 응답결과를 처리하는 DTO 토스에서 날아오는 결제승인결과 데이터
public class TossConfirmResponse {
    private String paymentKey;
    private String orderId;
    private String status;
    private Long totalAmount;
    private String method;      // CARD, EASY_PAY 등
    private String requestedAt;
    private String approvedAt;
    // 필요하면 카드/계좌 상세 정보 필드 추가
}
