package com.rootcore.auth.mapper;

import com.rootcore.auth.vo.MenuAuthSaveVO;
import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.MenuTreeVO;
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

    /** 2) 사용자 + 메뉴트리 + 권한 조회 */
    List<MenuTreeVO> selectUserMenuTree(
            @Param("companyCode") String companyCode,
            @Param("userId") String userId
    );

    /** 3) 기존 사용자 권한 삭제 */
    int deleteUserMenuAuth(
            @Param("companyCode") String companyCode,
            @Param("userId") String userId
    );

    /** 4) 사용자 메뉴 권한 INSERT */
    int insertUserMenuAuth(MenuAuthSaveVO vo);

    /** 5) 로그인 후 세션용 권한 조회 */
    List<MenuAuthSaveVO> selectUserMenuAuth(
            @Param("companyCode") String companyCode,
            @Param("userId") String userId
    );
}
