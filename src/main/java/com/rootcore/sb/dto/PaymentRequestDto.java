package com.rootcore.sb.dto;

import lombok.Data;

@Data
public class PaymentRequestDto {

    private String orderName;
    private Long amount;
    private String customerEmail;
    private String customerName;

   
}
