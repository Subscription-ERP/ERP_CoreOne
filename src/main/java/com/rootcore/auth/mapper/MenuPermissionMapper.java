package com.rootcore.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.UserAuthVO;

@Mapper
public interface MenuPermissionMapper {

    // ✅ USER 목록 조회 (검색)
    List<UserAuthVO> selectUserList(
        @Param("companyCode") String companyCode,
        @Param("userName") String userName,
        @Param("dept") String dept,
        @Param("position") String position
    );

    // ✅ ROLE → MENU 권한 조회
    List<RoleMenuAuthVO> selectRoleMenuAuthList(
        @Param("companyCode") String companyCode,
        @Param("roleCode") String roleCode
    );

    // ✅ ROLE → MENU 권한 삭제
    int deleteRoleMenuAuth(
        @Param("companyCode") String companyCode,
        @Param("roleCode") String roleCode
    );

    // ✅ ROLE → MENU 권한 등록
    int insertRoleMenuAuth(RoleMenuAuthVO vo);
}
