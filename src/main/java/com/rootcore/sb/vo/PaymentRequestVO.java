package com.rootcore.sb.vo;

import lombok.Data;

@Data
//사용자가 결제를 누르기전에 주문정보로
//결제창을 생성하기위한 서버내부요청 데이터
public class PaymentRequestVO {

    private String orderName;
    private Long amount;
    private String customerEmail;
    private String customerName;

   
}
