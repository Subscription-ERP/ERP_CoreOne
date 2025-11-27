package com.rootcore.sb.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TossConfirmResponseDto {

    private String paymentKey;
    private String orderId;
    private String status;          // SUCCESS, FAILED 등
    private Long totalAmount;
    private LocalDateTime approvedAt;

   
}
