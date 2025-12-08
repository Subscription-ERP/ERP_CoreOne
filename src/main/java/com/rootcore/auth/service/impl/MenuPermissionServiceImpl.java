package com.rootcore.auth.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.auth.mapper.MenuPermissionMapper;
import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.UserAuthVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuPermissionServiceImpl implements MenuPermissionService {

    private final MenuPermissionMapper mapper;

    // ✅ USER 목록 조회
    @Override
    public List<UserAuthVO> getUserList(
            String companyCode,
            String userName,
            String dept,
            String position) {

        return mapper.selectUserList(companyCode, userName, dept, position);
    }

    // ✅ ROLE → MENU 권한 조회
    @Override
    public List<RoleMenuAuthVO> getRoleMenuAuthList(
            String companyCode,
            String roleCode) {

        return mapper.selectRoleMenuAuthList(companyCode, roleCode);
    }

    // ✅ ROLE → MENU 권한 저장
    @Transactional
    @Override
    public int saveRoleMenuAuth(List<RoleMenuAuthVO> list) {

        if (list == null || list.isEmpty()) {
            return 0;
        }

        String companyCode = list.get(0).getCompanyCode();
        String roleCode    = list.get(0).getRoleCode();

        // ✅ 기존 ROLE 권한 전체 삭제
        mapper.deleteRoleMenuAuth(companyCode, roleCode);

        // ✅ 새 권한 등록
        int result = 0;
        for (RoleMenuAuthVO vo : list) {
            result += mapper.insertRoleMenuAuth(vo);
        }

        return result;
    }
}
