package com.rootcore.auth.web;

import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.MenuAuthSaveVO;
import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.MenuTreeVO;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/menu_permission/api")
public class MenuPermissionController {

    private final MenuPermissionService service;

    /** 1) 사용자 검색 조회 */
    @GetMapping("/user")
    public List<MenuPermissionUserVO> getUserList(
            @RequestParam String companyCode,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) String position
    ) {
        return service.getUserList(companyCode, userName, dept, position);
    }

    /** 2) 선택한 사용자 메뉴 권한 조회 */
    @GetMapping("/menu")
    public List<MenuTreeVO> getUserMenuAuth(
            @RequestParam String companyCode,
            @RequestParam String userId
    ) {
        return service.getUserMenuTree(companyCode, userId);
    }

    /** 3) 권한 저장 */
    @PostMapping("/save")
    public int saveMenuAuth(@RequestBody List<MenuAuthSaveVO> authList) {
        return service.saveMenuAuth(authList);
    }
}
