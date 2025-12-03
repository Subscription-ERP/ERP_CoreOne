package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class MenuTreeRowVO {

    private String menuCode;
    private String menuName;
    private String parentMenuCode;
    private String menuGroup;
    private String menuUrl;
    private Integer menuLevel;
    private Integer sortOrder;

    // ★ 메뉴 권한
    private String readAuth;     // 'Y' / 'N'
    private String createAuth;   // 'Y' / 'N'
    private String updateAuth;   // 'Y' / 'N'
    private String deleteAuth;   // 'Y' / 'N'
}
