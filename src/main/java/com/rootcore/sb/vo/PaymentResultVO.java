package com.rootcore.sb.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
//결제승인까지 완료시 사용자에게 보여줄 결과페이지 데이터
public class PaymentResultVO {
    private boolean success;
    private String orderId;
    private String paymentKey;
    private Long amount;
    private String method;   // 카드/간편결제 등
    private String status;   // DONE, CANCELED...
    private String message;  // "결제 성공" / "결제 실패" 등
}