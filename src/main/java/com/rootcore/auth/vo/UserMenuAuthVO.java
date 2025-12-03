package com.rootcore.auth.vo;

import lombok.Data;

/**
 * 로그인 성공 시 세션에 담을
 * 사용자별 메뉴/액션 권한 한 줄
 */
@Data
public class UserMenuAuthVO {

    private String companyCode;
    private String userId;
    private String menuCode;
    private String actionCode;   // READ / CREATE / UPDATE / DELETE
    private String authYn;       // Y / N
}
