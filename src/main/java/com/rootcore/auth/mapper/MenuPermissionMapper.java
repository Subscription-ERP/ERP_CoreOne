package com.rootcore.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.RoleVO;

@Mapper
public interface MenuPermissionMapper {

    // =====================================================
    // ✅ [관리자 화면용]
    // =====================================================

    // ROLE 목록 조회
    List<RoleVO> selectRoleList(
            @Param("companyCode") String companyCode,
            @Param("roleCode") String roleCode
    );

    // ROLE → MENU 권한 조회 (관리자 화면용)
    List<RoleMenuAuthVO> selectRoleMenuAuthList(
            @Param("companyCode") String companyCode,
            @Param("roleCode") String roleCode
    );

    // ROLE → MENU 기존 권한 전체 삭제
    int deleteRoleMenuAuth(
            @Param("companyCode") String companyCode,
            @Param("roleCode") String roleCode
    );

    // ROLE → MENU 권한 저장
    int insertRoleMenuAuth(RoleMenuAuthVO vo);


    // =====================================================
    // ✅ [로그인 사용자용 - ⭐ 이번에 추가된 핵심]
    // =====================================================

    /**
     * ✅ 로그인 사용자용 실제 메뉴 조회
     * - READ_YN = 'Y' 인 메뉴만 조회
     * - sidebarMenu.js 에서 사용
     */
    List<RoleMenuAuthVO> selectLoginMenuList(
            @Param("companyCode") String companyCode,
            @Param("roleCode") String roleCode
    );
}