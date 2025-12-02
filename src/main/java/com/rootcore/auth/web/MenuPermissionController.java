package com.rootcore.auth.web;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.MenuTreeVO;
import com.rootcore.auth.vo.MenuAuthSaveVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/menu_permission")
@RequiredArgsConstructor
public class MenuPermissionController {

    private final MenuPermissionService service;

    private String getCompanyCode() { return "ROOT"; }
    private String getLoginUser() { return "ADMIN"; }

    /** 사용자 목록 조회 */
    @GetMapping("/users")
    public List<MenuPermissionUserVO> getUsers(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) String position
    ) {
        return service.getUserList(getCompanyCode(), userName, dept, position);
    }

    /** 메뉴 트리 조회 */
    @GetMapping("/tree")
    public List<MenuTreeVO> getMenuTree(
            @RequestParam String userId,
            @RequestParam String menuGroup
    ) {
        return service.getMenuTree(getCompanyCode(), userId, menuGroup);
    }

    /** 메뉴권한 저장 */
    @PostMapping("/save")
    public String saveMenuAuth(
            @RequestBody List<MenuAuthSaveVO> authList,
            @RequestParam String userId
    ) {
        service.saveUserAuth(getCompanyCode(), userId, authList, getLoginUser());
        return "OK";
    }
}
