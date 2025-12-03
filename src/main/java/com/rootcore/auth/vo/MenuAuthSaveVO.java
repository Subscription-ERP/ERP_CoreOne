package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class MenuAuthSaveVO {

    private String menuCode;   // 메뉴 코드
    private String actionCode; // READ / CREATE / UPDATE / DELETE
    private String authYn;     // Y / N
    private String companyCode;
    private String userId;
}
