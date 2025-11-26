package com.rootcore.sb.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Payment {

    private Long id;
    private String orderId;
    private String orderName;
    private Long amount;
    private String status;       // READY, SUCCESS, FAIL 등
    private String paymentKey;   // 승인 후 저장
    private String customerEmail;
    private String customerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // getter/setter 생략
    // ... 전부 만들어주면 됨
}
