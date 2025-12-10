package com.rootcore.sb.vo;

import lombok.Data;

@Data
//토스 승인 api요청 데이터 
public class TossBillingConfirmRequestVO {
    private String billingKey; // URL path로 들어가도 되고 별도 필드로 둬도 됨
    private Long amount;
    private String orderId;
    private String orderName;
    private String customerKey;
    // 우리 서비스 내부 비즈니스용
    private String subCode;  // ← DB 업데이트에 꼭 필요

}
