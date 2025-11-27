package com.rootcore.sb.dto;

import lombok.Data;

@Data
public class PaymentReadyResponseDto {

    private String orderId;
    private Long amount;
    private String orderName;
    private String successUrl;
    private String failUrl;

}