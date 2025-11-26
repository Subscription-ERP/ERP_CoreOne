package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class LoginFindIdVO {

    private String companyCode;   // 회사코드
    private String userId;        // 사용자ID
    private String userName;      // 사용자 이름
    private String email;         // 이메일 (입력값)
}
