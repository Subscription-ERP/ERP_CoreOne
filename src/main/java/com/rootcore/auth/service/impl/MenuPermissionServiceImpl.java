package com.rootcore.auth.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.auth.mapper.MenuPermissionMapper;
import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.MenuTreeVO;
import com.rootcore.auth.vo.MenuAuthSaveVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuPermissionServiceImpl implements MenuPermissionService {

    private final MenuPermissionMapper mapper;

    @Override
    public List<MenuPermissionUserVO> getUserList(String companyCode, String userName, String dept, String position) {
        return mapper.selectUserList(companyCode, userName, dept, position);
    }

    @Override
    public List<MenuTreeVO> getMenuTree(String companyCode, String userId, String menuGroup) {
        return mapper.selectMenuTree(companyCode, userId, menuGroup);
    }

    @Override
    @Transactional
    public void saveUserAuth(String companyCode, String userId, List<MenuAuthSaveVO> authList, String loginUserId) {

        mapper.deleteUserMenuAuth(companyCode, userId);

        for (MenuAuthSaveVO vo : authList) {
            vo.setUpdatedBy(loginUserId);
            mapper.insertUserMenuAuth(vo);
        }
    }
}
