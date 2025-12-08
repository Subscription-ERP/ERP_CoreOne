package com.rootcore.common.vo;

import lombok.Data; // ★ getter/setter 자동 생성

@Data
public class BaseInfoVO {
    private String companyCode;
    private String companyName;
    private String ceoName;
    private String ceoPhone;
    private String companyEmail;
    private String industryType;
    private String businessType;
    private String managerName;
    private int employeeCount;
    private String bno;
    private String checkNo;
    private String companyAddress;
    private String companyPhone;

}