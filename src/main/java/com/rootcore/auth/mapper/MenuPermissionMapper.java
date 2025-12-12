package com.rootcore.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.RoleVO;

@Mapper
public interface MenuPermissionMapper {

    // ==============================
    // 1) 사용자 목록 조회 (LEFT GRID)
    // ==============================
    List<MenuPermissionUserVO> selectUserList(
            @Param("companyCode") String companyCode,
            @Param("userName") String userName,
            @Param("dept") String dept,
            @Param("position") String position
    );

    // ==============================
    // 2) ROLE 목록 조회 (CENTER GRID)
    // ==============================
    List<RoleVO> selectRoleList(
            @Param("companyCode") String companyCode,
            @Param("roleCode") String roleCode
    );

    // ==============================
    // 3) ROLE → MENU 권한 조회/저장 (RIGHT GRID)
    // ==============================
    List<RoleMenuAuthVO> selectRoleMenuAuthList(
            @Param("companyCode") String companyCode,
            @Param("roleCode") String roleCode
    );

    int deleteRoleMenuAuth(
            @Param("companyCode") String companyCode,
            @Param("roleCode") String roleCode
    );

    int insertRoleMenuAuth(RoleMenuAuthVO vo);

    // ==============================
    // 4) 로그인 사용자용 메뉴 조회 (기존 그대로)
    // ==============================
    List<RoleMenuAuthVO> selectLoginMenuList(
            @Param("companyCode") String companyCode,
            @Param("roleCode") String roleCode
    );

    // ==============================
    // 5) 선택 사용자 ROLE 일괄 변경
    // ==============================
    int updateUserRoleBatch(
            @Param("companyCode") String companyCode,
            @Param("roleCode") String roleCode,
            @Param("userIds") List<String> userIds
    );
}
