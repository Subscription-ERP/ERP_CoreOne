package com.rootcore.auth.vo;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

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
    
    // LoginVO 맨 아래에 추가
    private String roleCode;
    
    // 생성자, 수정자
    private String createdBy;
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    private String updatedBy;
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;

}