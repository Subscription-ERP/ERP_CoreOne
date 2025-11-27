package com.rootcore.sb.vo;

import lombok.Data;

@Data
//서버가 결제 준비 단계에서 결제창 실행에 필요한 데이터(주문번호, URL 등)
//를 프론트로 주는 응답
//{
//"orderId": "ORD-xxxx",
//"orderName": "아이폰14",
//"amount": 1000000
//} 이데이터를 기반으로 프론트에서 toss 결제창열림(주문번호)
public class PaymentReadyResponseVO {

    private String orderId;
    private Long amount;
    private String orderName;
    private String successUrl;
    private String failUrl;

}