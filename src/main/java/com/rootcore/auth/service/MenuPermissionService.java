package com.rootcore.auth.service;

import java.util.List;

import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.RoleVO;

public interface MenuPermissionService {

    //ROLE 목록 조회
    //    - 검색조건(ROLE_CODE)
    List<RoleVO> getRoleList(String companyCode, String roleCode);

    // ROLE → MENU 권한 조회 (운영급 정책 적용본)
    //	 - DB에 저장된 권한 + ROLE 정책(USER / MANAGER / ADMIN)
    //      - ADMIN: 전체 권한
    //      - MANAGER: 조회 + 등록 + 수정
    //      - USER: 조회만 가능
    List<RoleMenuAuthVO> getRoleMenuAuthList(String companyCode, String roleCode);

    //ROLE → MENU 권한 저장
    //      - ADMIN 저장 금지
    //      - MANAGER 삭제 권한 강제 N 처리
    //      - 기존 권한 전체 삭제 후 재등록  
    int saveRoleMenuAuth(List<RoleMenuAuthVO> list);
}
