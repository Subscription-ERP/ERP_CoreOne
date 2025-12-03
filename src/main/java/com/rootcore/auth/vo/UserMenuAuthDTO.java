package com.rootcore.auth.vo;

import lombok.Data;

/**
 * 사용자별 메뉴권한 조회용 DTO (DB 조회 결과 한 줄)
 * - TB_MENU_MASTER + TB_USER_MENU_AUTH 조인 결과
 */
@Data
public class UserMenuAuthDTO {

    private String menuCode;
    private String menuName;
    private String parentMenuCode;
    private Integer menuLevel;
    private Integer sortOrder;
    private String menuGroup;     // HR / SALES / FI 등

    private String readAuth;      // 'Y' / 'N'
    private String createAuth;
    private String updateAuth;
    private String deleteAuth;
}
