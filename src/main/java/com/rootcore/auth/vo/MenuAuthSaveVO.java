package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class MenuAuthSaveVO {
    private String companyCode;
    private String userId;
    private String menuCode;

    private String readAuth;  
    private String createAuth;
    private String updateAuth;
    private String deleteAuth;

    private String updatedBy;
}
