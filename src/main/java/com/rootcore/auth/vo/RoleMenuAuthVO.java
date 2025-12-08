package com.rootcore.auth.vo;

import lombok.Data;

@Data
public class RoleMenuAuthVO {

    private String companyCode;
    private String roleCode;
    private String menuCode;

    private String readYn;
    private String createYn;
    private String updateYn;
    private String deleteYn;

    private String createdBy;
    private String updatedBy;
}
