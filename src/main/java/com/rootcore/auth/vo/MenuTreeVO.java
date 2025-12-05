package com.rootcore.auth.vo;

import lombok.Data;
import java.util.List;

@Data
public class MenuTreeVO {

    // 메뉴 기본 정보
    private String menuCode;      // MENU_CODE
    private String menuName;      // MENU_NAME
    private String parentCode;    // PARENT_MENU_CODE
    private String systemType;    // SYSTRM_TYPE (SYS, HR, SALES, FI, SUB ...)
    private int sortOrder;        // SORT_ORDER

    // 메뉴별 액션 리스트 (동적)
    private List<MenuActionVO> actions;

    // 하위 메뉴
    private List<MenuTreeVO> children;
}
