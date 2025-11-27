package com.rootcore.sb.vo;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
//Toss Payments에서 결제를 승인한 후 내려주는 응답 모델
//결제 성공/실패 관련 핵심 정보가 포함됨
public class TossConfirmResponseVO {

    private String paymentKey;
    private String orderId;
    private String status;          // SUCCESS, FAILED 등
    private Long totalAmount;
    private OffsetDateTime approvedAt;

   
}
