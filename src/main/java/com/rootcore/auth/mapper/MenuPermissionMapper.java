package com.rootcore.auth.mapper;

import com.rootcore.auth.vo.MenuAuthSaveVO;
import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.MenuActionRowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuPermissionMapper {

    /** 1) 검색조건 포함 사용자 목록 조회 */
    List<MenuPermissionUserVO> selectUserList(
            @Param("companyCode") String companyCode,
            @Param("userName") String userName,
            @Param("dept") String dept,
            @Param("position") String position
    );

    /** 2) 사용자 + 메뉴 + 액션 + 권한 조회 (평탄 구조) */
    List<MenuActionRowVO> selectUserMenuTree(
            @Param("companyCode") String companyCode,
            @Param("userId") String userId
    );

    /** 3) 기존 사용자 권한 삭제 */
    int deleteUserMenuAuth(
            @Param("companyCode") String companyCode,
            @Param("userId") String userId
    );

    /** 4) 사용자 메뉴 권한 INSERT (동적 액션) */
    int insertUserMenuAuth(MenuAuthSaveVO vo);

    /** 5) 로그인 후 세션용 최종 권한 조회 (ROLE + USER 합산) */
    List<MenuAuthSaveVO> selectUserMenuAuth(
            @Param("companyCode") String companyCode,
            @Param("userId") String userId
    );
}
