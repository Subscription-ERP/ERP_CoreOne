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

     
    // ROLE → MENU 권한 조회
    // ADMIN만 강제 FULL
    // USER / MANAGER는 DB 값 그대로 사용
    @Override
    public List<RoleMenuAuthVO> getRoleMenuAuthList(String companyCode, String roleCode) {

        List<RoleMenuAuthVO> list =
                mapper.selectRoleMenuAuthList(companyCode, roleCode);

        if (list == null || list.isEmpty()) {
            return list;
        }

        // ADMIN만 정책 강제 적용
        if ("ADMIN".equals(roleCode)) {
            for (RoleMenuAuthVO vo : list) {
                applyRolePolicy(vo, roleCode);
            }
        }

        // USER / MANAGER는 DB 값 그대로 반환
        return list;
    }

    
    // ROLE → MENU 권한 저장
    // ADMIN만 보호
    // USER / MANAGER는 화면 설정 그대로 저장  
    @Override
    @Transactional
    public int saveRoleMenuAuth(List<RoleMenuAuthVO> list) {

        if (list == null || list.isEmpty()) {
            return 0;
        }

        String companyCode = list.get(0).getCompanyCode();
        String roleCode = list.get(0).getRoleCode();

        // ADMIN 권한은 수정 금지
        if ("ADMIN".equals(roleCode)) {
            return 1;
        }

        // 기존 권한 전체 삭제
        mapper.deleteRoleMenuAuth(companyCode, roleCode);

        // 신규 권한 저장
        int result = 0;
        for (RoleMenuAuthVO vo : list) {

            // NULL 방지 처리만 적용
            applyRolePolicy(vo, roleCode);

            result += mapper.insertRoleMenuAuth(vo);
        }

        return result;
    }

    
    // ROLE 정책 적용 로직 (최종 안정판)
    // ADMIN만 강제 FULL
    // 나머지는 N 보정만 수행
    private void applyRolePolicy(RoleMenuAuthVO vo, String roleCode) {

        if ("ADMIN".equals(roleCode)) {
            vo.setReadYn("Y");
            vo.setCreateYn("Y");
            vo.setUpdateYn("Y");
            vo.setDeleteYn("Y");
            return;
        }

        // USER / MANAGER는 DB 값 그대로 두고
        // 혹시 null이면 N으로만 보정
        if (vo.getReadYn()   == null) vo.setReadYn("N");
        if (vo.getCreateYn() == null) vo.setCreateYn("N");
        if (vo.getUpdateYn() == null) vo.setUpdateYn("N");
        if (vo.getDeleteYn() == null) vo.setDeleteYn("N");
    }
}
