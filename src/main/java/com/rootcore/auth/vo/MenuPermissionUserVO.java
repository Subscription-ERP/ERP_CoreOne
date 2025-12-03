package com.rootcore.auth.vo;

import lombok.Data;

//조회용 : 사용자 목록
@Data
public class MenuPermissionUserVO {
 private String companyCode;
 private String userId;
 private String userName;
 private String deptName;
 private String positionName;
 private String status;
}