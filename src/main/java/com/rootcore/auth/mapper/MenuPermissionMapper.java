package com.rootcore.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.RoleVO;

@Mapper
public interface MenuPermissionMapper {

    // ROLE 목록 조회
    List<RoleVO> selectRoleList(
            @Param("companyCode") String companyCode,
            @Param("roleCode") String roleCode
    );

    // ROLE → MENU 권한 조회 
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
}
