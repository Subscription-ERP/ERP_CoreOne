package com.rootcore.sb.vo;

import lombok.Data;

@Data
//토스 승인 api요청 데이터 
public class TossBillingConfirmRequestVO {
    private String billingKey; // URL path로 들어가도 되고 별도 필드로 둬도 됨
    private Long amount;
    private String orderId;
}
