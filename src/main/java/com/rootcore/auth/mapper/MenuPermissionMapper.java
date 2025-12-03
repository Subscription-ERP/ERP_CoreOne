package com.rootcore.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.MenuTreeRowVO;
import com.rootcore.auth.vo.UserMenuAuthVO;

@Mapper
public interface MenuPermissionMapper {

    /** 사용자 목록 조회 */
    List<MenuPermissionUserVO> selectUserList(
        @Param("companyCode") String companyCode,
        @Param("userName") String userName,
        @Param("dept") String dept,
        @Param("position") String position
    );

    /** 메뉴트리 + 사용자 권한 조회 */
    List<MenuTreeRowVO> selectMenuTree(
        @Param("companyCode") String companyCode,
        @Param("userId") String userId,
        @Param("menuGroup") String menuGroup
    );

    /** 기존 권한 삭제 */
    void deleteUserAuth(
        @Param("companyCode") String companyCode,
        @Param("userId") String userId
    );

    /** 액션별 권한 INSERT */
    void insertUserAuth(
        @Param("companyCode") String companyCode,
        @Param("userId") String userId,
        @Param("menuCode") String menuCode,
        @Param("actionCode") String actionCode,
        @Param("authYn") String authYn,
        @Param("updatedBy") String updatedBy
    );

    /** 로그인 성공 시 세션에 담아 줄 사용자 전체 권한 조회 */
    List<UserMenuAuthVO> selectUserAuthList(
        @Param("companyCode") String companyCode,
        @Param("userId") String userId
    );
}
