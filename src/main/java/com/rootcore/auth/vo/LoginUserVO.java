package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class LoginUserVO {

    private String companyCode;
    private String userId;
    private String userName;
    private String dept;
    private String deptName;
    private String jobTitle;
    private String jobTitleName;
    private String position;
    private String positionName;
    private String userPhoto;
    private String roleCode;   // DB 컬럼 ROLE_CODE
}
