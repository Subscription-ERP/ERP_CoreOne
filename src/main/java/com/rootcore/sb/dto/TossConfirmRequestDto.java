package com.rootcore.sb.dto;

import lombok.Data;

@Data
public class TossConfirmRequestDto {

    private String paymentKey;
    private String orderId;
    private Long amount;

   
}
