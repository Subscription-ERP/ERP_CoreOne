package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class LoginUserVO {
    private String companyCode;
    private String userId;
    private String userName;
    private String userRole;
}
