package com.rootcore.sb.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NewSubsPaymentVO {
    // 회사 정보
    private String companyName;
    private String ceoName;
    private String ceoPhone;
    private String companyEmail;
    private String industryType;
    private String businessType;
    private String managerName;
    private Integer employeeCount;
    private String bno;
    private String checkNo;
    private String companyAddress;
    private String companyPhone;
    private String managerPhone;

    // 구독 정보
    private String planCode;
    private LocalDateTime subsStart;
    private LocalDateTime subsEnd;

    // 결제 정보
    private Long totalPrice;
    private String paymentMethod;
    private String paymentKey;
    private String orderId;
    private String cardCompany;
}
