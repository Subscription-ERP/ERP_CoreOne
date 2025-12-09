package com.rootcore.hr.vo;

import lombok.Data;

@Data
public class DeptMasterVO {
    private String deptCode;
    private String companyCode;
    private String deptName;
    private String upperDeptNo;
    private String deptLevel;
    private String startDate;
    private String endDate;
    private String status;
    private String deptMng;
    private String rm;
}