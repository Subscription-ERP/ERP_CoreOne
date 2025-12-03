package com.rootcore.auth.service;

import java.util.List;

import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.MenuTreeVO;
import com.rootcore.auth.vo.MenuAuthSaveVO;

public interface MenuPermissionService {

    List<MenuPermissionUserVO> getUserList(
            String companyCode, String userName, String dept, String position);

    List<MenuTreeVO> getMenuTree(String companyCode, String userId, String menuGroup);

    void saveUserAuth(
            String companyCode, String userId,
            List<MenuAuthSaveVO> authList, String updatedBy);
}

