package com.rootcore.sb.vo;

import lombok.Data;

@Data
public class ExistSubsPaymentVO {
    private String subCode;
    private Long totalPrice;
    private String paymentMethod;
    private String paymentKey;
    private String orderId;
    private String cardCompany;
}
