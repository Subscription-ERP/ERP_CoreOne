package com.rootcore.auth.service.impl;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.auth.mapper.MenuPermissionMapper;
import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuPermissionServiceImpl implements MenuPermissionService {

    private final MenuPermissionMapper mapper;

    @Override
    public List<MenuPermissionUserVO> getUserList(
            String companyCode, String userName, String dept, String position) {

        return mapper.selectUserList(companyCode, userName, dept, position);
    }

    @Override
    public List<MenuTreeVO> getMenuTree(String companyCode, String userId, String menuGroup) {

        List<MenuTreeRowVO> rows = mapper.selectMenuTree(companyCode, userId, menuGroup);

        Map<String, MenuTreeVO> map = new HashMap<>();

        for (MenuTreeRowVO r : rows) {

            MenuTreeVO node = map.getOrDefault(r.getMenuCode(), new MenuTreeVO());
            node.setMenuCode(r.getMenuCode());
            node.setMenuName(r.getMenuName());
            node.setParentMenuCode(r.getParentMenuCode());
            node.setMenuGroup(r.getMenuGroup());
            node.setMenuUrl(r.getMenuUrl());
            node.setMenuLevel(r.getMenuLevel());
            node.setSortOrder(r.getSortOrder());

            // 액션 리스트 구성
            List<MenuActionVO> actions = new ArrayList<>();
            actions.add(new MenuActionVO("READ",   "Y".equals(r.getReadAuth())));
            actions.add(new MenuActionVO("CREATE", "Y".equals(r.getCreateAuth())));
            actions.add(new MenuActionVO("UPDATE", "Y".equals(r.getUpdateAuth())));
            actions.add(new MenuActionVO("DELETE", "Y".equals(r.getDeleteAuth())));

            node.setActions(actions);

            map.put(r.getMenuCode(), node);
        }

        // 부모-자식 트리 구성
        List<MenuTreeVO> rootList = new ArrayList<>();

        for (MenuTreeVO node : map.values()) {
            if (node.getParentMenuCode() == null || node.getParentMenuCode().trim().isEmpty()) {
                rootList.add(node);
            } else {
                MenuTreeVO parent = map.get(node.getParentMenuCode());
                if (parent != null) parent.getChildren().add(node);
            }
        }

        rootList.sort(Comparator.comparing(MenuTreeVO::getSortOrder));

        return rootList;
    }


    /* ==============================
        저장 (액션 한 줄씩 insert)
    ================================= */
    @Transactional
    @Override
    public void saveUserAuth(
            String companyCode, String userId,
            List<MenuAuthSaveVO> authList, String updatedBy) {

        mapper.deleteUserAuth(companyCode, userId);

        for (MenuAuthSaveVO vo : authList) {
            mapper.insertUserAuth(
                    companyCode,
                    userId,
                    vo.getMenuCode(),
                    vo.getActionCode(),
                    vo.getAuthYn(),
                    updatedBy
            );
        }
    }
}
