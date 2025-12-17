package com.rootcore.auth.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.auth.mapper.MenuPermissionMapper;
import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.RoleVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuPermissionServiceImpl implements MenuPermissionService {

    private final MenuPermissionMapper mapper;

    // ==========================
    // USER 목록 조회 (LEFT GRID)
    // ==========================
    @Override
    public List<MenuPermissionUserVO> getUserList(
            String companyCode,
            String userName,
            String dept,
            String position) {

        return mapper.selectUserList(companyCode, userName, dept, position);
    }

    // ==========================
    // ROLE 목록 조회 (CENTER GRID)
    // ==========================
    @Override
    public List<RoleVO> getRoleList(String companyCode, String roleCode) {
        return mapper.selectRoleList(companyCode, roleCode);
    }

    // ==========================
    // ROLE → MENU 권한 조회 (RIGHT)
    // ==========================
    @Override
    public List<RoleMenuAuthVO> getRoleMenuAuthList(String companyCode, String roleCode) {

        List<RoleMenuAuthVO> list = mapper.selectRoleMenuAuthList(companyCode, roleCode);

        if (list == null || list.isEmpty()) {
            return list;
        }

        // ADMIN 은 화면에서 항상 전부 Y 보여주고 싶다면 여기서도 보정 가능
        if ("ADMIN".equals(roleCode)) {
            for (RoleMenuAuthVO vo : list) {
                vo.setReadYn("Y");
                vo.setCreateYn("Y");
                vo.setUpdateYn("Y");
                vo.setDeleteYn("Y");
            }
        }

        return list;
    }

    // ==========================
    // ROLE → MENU 권한 저장
    // ==========================
    @Override
    @Transactional
    public int saveRoleMenuAuth(List<RoleMenuAuthVO> list) {

        if (list == null || list.isEmpty()) {
            return 0;
        }

        String companyCode = list.get(0).getCompanyCode();
        String roleCode    = list.get(0).getRoleCode();

        // ADMIN 보호 (DB 저장 X, 화면에서만 강제 Y)
        if ("ADMIN".equals(roleCode)) {
            return 1;
        }

        // 기존 권한 전체 삭제
        mapper.deleteRoleMenuAuth(companyCode, roleCode);

        int result = 0;
        for (RoleMenuAuthVO vo : list) {
            applyRolePolicy(vo, roleCode);
            result += mapper.insertRoleMenuAuth(vo);
        }

        return result;
    }

    // ROLE 정책 (필요시 MANAGER / USER 별 정책 추가)
    private void applyRolePolicy(RoleMenuAuthVO vo, String roleCode) {

        // 기본 null → 'N' 처리
        if (vo.getReadYn()   == null) vo.setReadYn("N");
        if (vo.getCreateYn() == null) vo.setCreateYn("N");
        if (vo.getUpdateYn() == null) vo.setUpdateYn("N");
        if (vo.getDeleteYn() == null) vo.setDeleteYn("N");
    }

    // ==========================
    // 로그인 사용자용 메뉴 조회 (⭐핵심 수정)
    // ==========================
    @Override
    public List<RoleMenuAuthVO> getLoginMenuList(String companyCode, String roleCode) {

        // ✅ ADMIN은 회사 코드 무시하고, 전체 메뉴 + 전체 권한
        if ("ADMIN".equals(roleCode)) {
            return mapper.selectAllMenuForAdmin();
        }

        // ✅ MANAGER / USER는 회사별 ROLE 권한
        return mapper.selectLoginMenuList(companyCode, roleCode);
    }

    // ==========================
    // 선택 사용자 ROLE 일괄 변경
    // ==========================
    @Override
    @Transactional
    public int updateUserRoleForUsers(String companyCode, String roleCode, List<String> userIds) {

        if (userIds == null || userIds.isEmpty()) {
            return 0;
        }

        return mapper.updateUserRoleBatch(companyCode, roleCode, userIds);
    }
}
