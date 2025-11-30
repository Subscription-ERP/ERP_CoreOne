package com.rootcore.auth.vo;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class LoginVO {

    // 로그인 입력값
    private String companyCode;
    private String userId;
    private String password;     // 사용자가 입력한 평문 비밀번호
    private boolean rememberId;

    // DB 값 (TB_LOGIN_MASTER)
    private String dbPassword;   // DB에 저장된 암호화된 비밀번호(PASSWORD 컬럼)
    private int failCount;
    private String status;       // ACTIVE / LOCKED
    private Date lockedDate;
    private Date lastLogin;
    private String userName;

    // 권한/역할 정보 (지금은 ROLE_USER 하나만 사용 예정)
    private List<String> roleList;
    private List<String> menuAuthList;

    // 비밀번호 재설정/인증 관련
    private String email;
    private String token;
    private Date expiredDate;

    // 내부 로직 처리용
    private boolean loginSuccess;
    private String message;
}
