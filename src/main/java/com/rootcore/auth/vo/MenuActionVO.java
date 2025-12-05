package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class MenuActionVO {

    private String actionCode;   // READ, CREATE, UPDATE, DELETE, APPROVE ...
    private String actionName;   // 조회, 등록, 수정, 삭제, 승인 ...
    
    private String authYn;       // 최종 권한 (Y/N)
    private String roleAuthYn;   // ROLE 기준 권한 (Y/N 또는 null)
    private String userAuthYn;   // USER 오버라이드 값 (Y/N 또는 null)
}
