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


    // ============================================================
    // ✅ 1. ROLE 목록 조회
    // ============================================================
    @Override
    public List<RoleVO> getRoleList(String companyCode, String roleCode) {
        return mapper.selectRoleList(companyCode, roleCode);
    }


    // ============================================================
    // ✅ 2. ROLE → MENU 권한 조회 (운영급 핵심)
    //
    // ROLE 정책 개요:
    // USER    → 조회(READ)만 가능
    // MANAGER → 조회 + 등록 + 수정 가능 (삭제는 N)
    // ADMIN   → 전체 가능 (READ/CREATE/UPDATE/DELETE 모두 Y)
    //
    // ※ DB에서 가져온 권한값이 있더라도
    //    정책에 맞춰 강제로 최종 값으로 재가공 (운영 안정성 보장)
    // ============================================================
    @Override
    public List<RoleMenuAuthVO> getRoleMenuAuthList(String companyCode, String roleCode) {

        // 1️⃣ 기본 DB 권한 조회
        List<RoleMenuAuthVO> list =
                mapper.selectRoleMenuAuthList(companyCode, roleCode);

        if (list == null || list.isEmpty()) {
            return list;
        }

        // 2️⃣ 역할 정책 적용 (핵심)
        for (RoleMenuAuthVO vo : list) {
            applyRolePolicy(vo, roleCode);
        }

        return list;
    }


    // ============================================================
    // ✅ 3. ROLE → MENU 권한 저장 (운영급 최종)
    //
    // 정책:
    //  - ADMIN 권한은 절대 덮어쓰기 불가 (보안 안정성)
    //  - 저장 시 기존 권한 전체 삭제 후 재등록
    // ============================================================
    @Override
    @Transactional
    public int saveRoleMenuAuth(List<RoleMenuAuthVO> list) {

        if (list == null || list.isEmpty()) {
            return 0;
        }

        String companyCode = list.get(0).getCompanyCode();
        String roleCode = list.get(0).getRoleCode();

        // ❗ ADMIN 보호 (누구도 ADMIN 권한을 덮어쓸 수 없음)
        if ("ADMIN".equals(roleCode)) {
            return 1;
        }

        // 1️⃣ 기존 권한 전체 삭제
        mapper.deleteRoleMenuAuth(companyCode, roleCode);

        // 2️⃣ 신규 권한 저장
        int result = 0;
        for (RoleMenuAuthVO vo : list) {
            // INSERT 전에 혹시라도 들어온 값이 정책과 다르면 보정
            applyRolePolicy(vo, roleCode);

            result += mapper.insertRoleMenuAuth(vo);
        }

        return result;
    }


    // ============================================================
    // ✅ ROLE 정책 적용 로직 (단일 책임 함수)
    //
    //  - ServiceImpl 내 모든 정책 처리는 이 메서드에서 수행
    //  - 조회 시 적용, 저장 시 적용 → 운영 안정성 200%
    // ============================================================
    private void applyRolePolicy(RoleMenuAuthVO vo, String roleCode) {

        switch (roleCode) {

            case "ADMIN":
                vo.setReadYn("Y");
                vo.setCreateYn("Y");
                vo.setUpdateYn("Y");
                vo.setDeleteYn("Y");
                break;

            case "MANAGER":
                vo.setReadYn("Y");
                vo.setCreateYn("Y");
                vo.setUpdateYn("Y");
                vo.setDeleteYn("N"); // 삭제는 MANAGER 제한
                break;

            case "USER":
            default:
                vo.setReadYn("Y");
                vo.setCreateYn("N");
                vo.setUpdateYn("N");
                vo.setDeleteYn("N");
                break;
        }
    }
}
