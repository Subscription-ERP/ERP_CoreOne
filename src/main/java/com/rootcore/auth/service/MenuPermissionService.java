package com.rootcore.auth.service;

import com.rootcore.auth.vo.MenuAuthSaveVO;
import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.MenuTreeVO;

import java.util.List;

public interface MenuPermissionService {

    //1) 검색조건 포함 사용자 목록 조회 */
    List<MenuPermissionUserVO> getUserList(
            String companyCode,
            String userName,
            String dept,
            String position
    );

    // 2) 사용자 메뉴트리 조회 
    List<MenuTreeVO> getUserMenuTree(String companyCode, String userId);

    // 3) 권한 저장 
    int saveMenuAuth(List<MenuAuthSaveVO> authList);
}
