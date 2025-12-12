package com.rootcore.auth.vo;


import lombok.Data;


@Data
public class RoleMenuAuthVO {


private String companyCode;
private String roleCode;
private String menuCode;
private String parentMenuCode;

private String readYn;
private String createYn;
private String updateYn;
private String deleteYn;


private String createdBy;
private String updatedBy;


private String menuName;
private String menuUrl; // 자동 메뉴 출력 핵심
private String systemType; // CM / FI / HR / SD

private Integer sortOrder;
}