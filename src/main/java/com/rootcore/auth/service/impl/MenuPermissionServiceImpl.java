package com.rootcore.auth.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.auth.mapper.MenuPermissionMapper;
import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.RoleVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuPermissionServiceImpl implements MenuPermissionService {

    private final MenuPermissionMapper mapper;

    // ROLE 목록 조회
    @Override
    public List<RoleVO> getRoleList(String companyCode, String roleCode) {
        return mapper.selectRoleList(companyCode, roleCode);
    }

    // ROLE → MENU 권한 조회 (관리자 화면용)
    // ADMIN → 전체 Y
    // MANAGER / USER → DB 그대로 (N 포함 절대 필터링 금지)
    @Override
    public List<RoleMenuAuthVO> getRoleMenuAuthList(String companyCode, String roleCode) {

        // DB에서 그대로 전부 조회
        List<RoleMenuAuthVO> list =
                mapper.selectRoleMenuAuthList(companyCode, roleCode);

        if (list == null || list.isEmpty()) {
            return list;
        }

        // ADMIN만 전체 강제 Y
        if ("ADMIN".equals(roleCode)) {
            for (RoleMenuAuthVO vo : list) {
                vo.setReadYn("Y");
                vo.setCreateYn("Y");
                vo.setUpdateYn("Y");
                vo.setDeleteYn("Y");
            }
        }

        // 여기서는 절대 FILTER 하면 안 된다
        return list;
    }


    // ROLE → MENU 권한 저장
    @Override
    @Transactional
    public int saveRoleMenuAuth(List<RoleMenuAuthVO> list) {

        if (list == null || list.isEmpty()) {
            return 0;
        }

        String companyCode = list.get(0).getCompanyCode();
        String roleCode = list.get(0).getRoleCode();

        // ADMIN 보호
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

    // ROLE 정책
    private void applyRolePolicy(RoleMenuAuthVO vo, String roleCode) {

        if ("ADMIN".equals(roleCode)) {
            vo.setReadYn("Y");
            vo.setCreateYn("Y");
            vo.setUpdateYn("Y");
            vo.setDeleteYn("Y");
            return;
        }

        if (vo.getReadYn()   == null) vo.setReadYn("N");
        if (vo.getCreateYn() == null) vo.setCreateYn("N");
        if (vo.getUpdateYn() == null) vo.setUpdateYn("N");
        if (vo.getDeleteYn() == null) vo.setDeleteYn("N");
    }


    // 로그인 사용자용 - 사이드바 전용
    // 여기서만 READ_YN = 'Y' 필터 적용하는 게 정답

    @Override
    public List<RoleMenuAuthVO> getLoginMenuList(String companyCode, String roleCode) {
        return mapper.selectLoginMenuList(companyCode, roleCode);
    }
}