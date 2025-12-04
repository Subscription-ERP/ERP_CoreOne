package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class MenuAuthSaveVO {

    private String companyCode;   // 회사코드
    private String userId;        // 사용자 ID
    private String menuCode;      // 메뉴코드
    private String actionCode;    // READ / CREATE / UPDATE / DELETE
    private String authYn;        // Y / N
}
