package com.rootcore.auth.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserRoleAssignRequest {

    private String roleCode;        // 부여할 ROLE
    private List<String> userIds;   // 대상 사용자ID 목록
}
