package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class MenuTreeVO {

    private String menuCode;
    private String parentMenuCode;
    private String menuName;
    private String menuUrl;
    private int menuLevel;
    private int sortOrder;

    private String readAuth;      // Y/N
    private String createAuth;    // Y/N
    private String updateAuth;    // Y/N
    private String deleteAuth;    // Y/N
}
