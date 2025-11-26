package com.rootcore.sb.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
//서버가 토스에게 승인요청을 위해 전달할 데이터를 처리하는 DTO
public class TossConfirmRequest {
    private String paymentKey;
    private String orderId;
    private Long amount;
}