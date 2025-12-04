package com.rootcore.auth.vo;

import lombok.Data;
import java.util.List;

@Data
public class MenuTreeVO {


    // 메뉴 기본 정보
    private String menuCode;       // 메뉴코드
    private String menuName;       // 메뉴명
    private String parentMenu;     // 상위 메뉴코드
    private int sortOrder;         // 정렬순서


    // 사용자 권한 (액션별)
    private String readAuth;       // READ 권한 (Y/N)
    private String createAuth;     // CREATE 권한
    private String updateAuth;     // UPDATE 권한
    private String deleteAuth;     // DELETE 권한


    // 자식 메뉴
    private List<MenuTreeVO> children;
}
