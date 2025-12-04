package com.rootcore.auth.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.auth.mapper.MenuPermissionMapper;
import com.rootcore.auth.service.MenuPermissionService;
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
    public List<MenuPermissionUserVO> getUserList(String companyCode, String userName, String dept, String position) {
        return mapper.selectUserList(companyCode, userName, dept, position);
    }

    /** 2) 메뉴트리 + 사용자 권한 */
    @Override
    public List<MenuTreeVO> getUserMenuTree(String companyCode, String userId) {
        return mapper.selectUserMenuTree(companyCode, userId);
    }

    /** 3) 권한 저장 */
    @Override
    public int saveMenuAuth(List<MenuAuthSaveVO> authList) {

        if (authList == null || authList.isEmpty()) return 0;

        String companyCode = authList.get(0).getCompanyCode();
        String userId = authList.get(0).getUserId();

        // 1) 기존 권한 삭제
        mapper.deleteUserMenuAuth(companyCode, userId);

        // 2) 새 권한 등록
        int count = 0;
        for (MenuAuthSaveVO vo : authList) {
            count += mapper.insertUserMenuAuth(vo);
        }

        return count;
    }
}
