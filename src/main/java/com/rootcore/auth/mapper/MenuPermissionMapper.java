package com.rootcore.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.MenuTreeVO;
import com.rootcore.auth.vo.MenuAuthSaveVO;

@Mapper
public interface MenuPermissionMapper {

    List<MenuPermissionUserVO> selectUserList(
            @Param("companyCode") String companyCode,
            @Param("userName") String userName,
            @Param("deptCode") String deptCode,
            @Param("positionCode") String positionCode
    );

    List<MenuTreeVO> selectMenuTree(
            @Param("companyCode") String companyCode,
            @Param("userId") String userId,
            @Param("menuGroup") String menuGroup
    );

    int deleteUserMenuAuth(
            @Param("companyCode") String companyCode,
            @Param("userId") String userId
    );

    int insertUserMenuAuth(MenuAuthSaveVO vo);
}
