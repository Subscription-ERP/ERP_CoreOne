package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class MenuActionRowVO {

    private String menuCode;
    private String menuName;
    private String parentCode;
    private String systemType;
    private int sortOrder;

    private String actionCode;
    private String actionName;

    private String roleAuthYn;   // ROLE 기준
    private String userAuthYn;   // USER 오버라이드
    private String authYn;       // NVL(user, role) 최종
}
