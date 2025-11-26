package com.rootcore.sb.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//토스 결제창에서 넘어올 데이터를 처리하는 DTO
public class PaymentConfirmRequest {
    private String paymentKey;
    private String orderId;
    private Long amount;
}