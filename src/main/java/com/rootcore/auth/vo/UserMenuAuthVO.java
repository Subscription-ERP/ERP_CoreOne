package com.rootcore.auth.vo;

import lombok.Data;

/**
 * 로그인한 사용자가 가진 메뉴 접근권한 정보를 담는 VO
 * - URL 직접 접근 차단
 * - 사이드바 출력 조건
 * - 버튼권한 제어(read/create/update/delete)
 */
@Data
public class UserMenuAuthVO {

    private String companyCode;  // 회사코드
    private String roleCode;     // ROLE 코드
    private String menuCode;     // 메뉴코드

    private String menuName;     // 메뉴명
    private String menuUrl;      // 메뉴 URL  (★ URL 접근권한 판단 핵심)

    private String systemType;   // HR, FI, SD, CM 등 그룹링 용도

    private String readYn;       // 조회 권한
    private String createYn;     // 등록 권한
    private String updateYn;     // 수정 권한
    private String deleteYn;     // 삭제 권한
}
