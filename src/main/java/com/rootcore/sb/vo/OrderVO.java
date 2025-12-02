package com.rootcore.sb.vo;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OrderVO {

    private String orderId;        // ORDER_ID
    private Long orderAmount;      // ORDER_AMOUNT
    private String orderStatus;    // ORDER_STATUS
    private String orderName;      // ORDER_NAME
    private String orderType;      // ORDER_TYPE
    private LocalDateTime billingStart;  // BILLING_START
    private LocalDateTime billingEnd;    // BILLING_END
    private String subCode;        // SUB_CODE
    private String planCode;       // PLAN_CODE
    private String companyCode;
    
    private String createdBy;      // CREATED_BY
    private LocalDateTime createDate;   // CREATE_DATE
    private String updatedBy;      // UPDATED_BY
    private LocalDateTime updateDate;   // UPDATE_DATE
}