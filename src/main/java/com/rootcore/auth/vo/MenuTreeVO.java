package com.rootcore.auth.vo;

import lombok.Data;
import java.util.List;

@Data
public class MenuTreeVO {

    private String menuCode;
    private String menuName;
    private String parentMenuCode;

    private String systemType;
    private int menuLevel;
    private int sortOrder;

    // ROLE 권한 4대 컬럼
    private String readYn;
    private String createYn;
    private String updateYn;
    private String deleteYn;

    private List<MenuTreeVO> children;
}
