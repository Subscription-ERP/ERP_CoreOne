package com.rootcore.fi.vo;

import lombok.Data;

@Data
public class MonthSlipVO {
    private String slipDate;     // 일자
    private String slipAccount;  // 회계계정
    private String accountName;  // 회계계정명
    private Long debitAmount;    // 차변금액
    private Long creditAmount;   // 대변금액
    private String custName;     // 거래처명
    private String remark;       // 적요
}
