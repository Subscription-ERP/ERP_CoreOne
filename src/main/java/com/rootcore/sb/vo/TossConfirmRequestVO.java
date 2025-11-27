package com.rootcore.sb.vo;

import lombok.Data;

@Data
//결제창에서 결제 완료 후
//successUrl로 전달되는 결제 승인 요청 데이터
public class TossConfirmRequestVO {

    private String paymentKey;
    private String orderId;
    private Long amount;

   
}
