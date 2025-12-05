package com.rootcore.auth.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rootcore.auth.mapper.MenuPermissionMapper;
import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.MenuActionRowVO;
import com.rootcore.auth.vo.MenuActionVO;
import com.rootcore.auth.vo.MenuAuthSaveVO;
import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.MenuTreeVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuPermissionServiceImpl implements MenuPermissionService {

    private final MenuPermissionMapper mapper;

    /** 1) 사용자 검색조건 조회 */
    @Override
    public List<MenuPermissionUserVO> getUserList(
            String companyCode, String userName, String dept, String position) {

        return mapper.selectUserList(companyCode, userName, dept, position);
    }

    /** 2) 메뉴트리 + 동적 액션 + ROLE기본 + USER오버라이드 */
    @Override
    public List<MenuTreeVO> getUserMenuTree(String companyCode, String userId) {

        List<MenuActionRowVO> rows = mapper.selectUserMenuTree(companyCode, userId);
        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }

        // 1) 메뉴 단위로 그룹핑
        Map<String, MenuTreeVO> menuMap = new LinkedHashMap<>();

        for (MenuActionRowVO row : rows) {
            MenuTreeVO menu = menuMap.computeIfAbsent(row.getMenuCode(), code -> {
                MenuTreeVO m = new MenuTreeVO();
                m.setMenuCode(code);
                m.setMenuName(row.getMenuName());
                m.setParentCode(row.getParentCode());
                m.setSystemType(row.getSystemType());
                m.setSortOrder(row.getSortOrder());
                m.setActions(new ArrayList<>());
                m.setChildren(new ArrayList<>());
                return m;
            });

            MenuActionVO action = new MenuActionVO();
            action.setActionCode(row.getActionCode());
            action.setActionName(row.getActionName());
            action.setRoleAuthYn(row.getRoleAuthYn());
            action.setUserAuthYn(row.getUserAuthYn());

            String authYn = row.getAuthYn();
            if (authYn == null) authYn = "N";
            action.setAuthYn(authYn);

            menu.getActions().add(action);
        }

        // 2) 트리 구조로 변환
        Map<String, MenuTreeVO> allMenus = menuMap;
        List<MenuTreeVO> rootList = new ArrayList<>();

        for (MenuTreeVO menu : allMenus.values()) {
            String parentCode = menu.getParentCode();
            if (parentCode == null || parentCode.isBlank() || !allMenus.containsKey(parentCode)) {
                rootList.add(menu);
            } else {
                allMenus.get(parentCode).getChildren().add(menu);
            }
        }

        // 3) 정렬
        Comparator<MenuTreeVO> menuComparator =
                Comparator.comparingInt(MenuTreeVO::getSortOrder)
                          .thenComparing(MenuTreeVO::getMenuCode);

        rootList.sort(menuComparator);
        allMenus.values().forEach(m ->
                m.getChildren().sort(menuComparator)
        );

        // 액션도 정렬 (SORT_ORDER 기준이 필요하다면 RowVO에 추가해서 정렬)
        allMenus.values().forEach(m ->
                m.getActions().sort(Comparator.comparing(MenuActionVO::getActionCode))
        );

        return rootList;
    }

    /** 3) 권한 저장 (USER 오버라이드 전체 재저장) */
    @Override
    public int saveMenuAuth(List<MenuAuthSaveVO> authList) {

        if (authList == null || authList.isEmpty()) return 0;

        String companyCode = authList.get(0).getCompanyCode();
        String userId      = authList.get(0).getUserId();

        // 1) 기존 사용자 권한 전체 삭제
        mapper.deleteUserMenuAuth(companyCode, userId);

        // 2) "체크된 것만" INSERT (authYn = Y 만 저장할지, N도 저장할지 선택 가능)
        int count = 0;
        for (MenuAuthSaveVO vo : authList) {
            // 여기서는 Y/N 모두 저장 (프런트에서 만든 payload 전체)
            count += mapper.insertUserMenuAuth(vo);
        }

        return count;
    }
}
