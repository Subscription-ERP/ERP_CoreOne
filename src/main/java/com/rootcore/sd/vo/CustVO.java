package com.rootcore.sd.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CustVO {
    private String companyCode;
    private String custCode;
    private String custName;
    private String ceoName;
    private String bno;
    private String bType;
    private String bItem;
    private String phone;
    private String faxNo;
    private String custEmail;
    private String address;
    private String addressDetail;
    private String custType;
    private String dept;
    private String userName;
    private char useStatus;
    private String createdBy;
    private Date createDate;
    private String updatedBy;
    private Date updateDate;
}
