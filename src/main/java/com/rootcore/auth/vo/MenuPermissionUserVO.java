// 패키지: com.rootcore.auth.vo
package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class MenuPermissionUserVO {
    private String companyCode;
    private String userId;
    private String userName;
    private String dept;        // 부서명 또는 코드
    private String jobTitle;    // 직급
    private String position;    // 직책
    private String userStatus;  // 재직상태 (0/1)
}
