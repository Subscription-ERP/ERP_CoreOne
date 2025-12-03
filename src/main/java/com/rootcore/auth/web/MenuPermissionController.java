package com.rootcore.auth.web;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.MenuAuthSaveVO;
import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.MenuTreeVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/menu_permission")
public class MenuPermissionController {

    private final MenuPermissionService service;

    /** 화면 이동 */
    @GetMapping
    public String menuPermissionPage() {
        return "auth/menu_permission";
    }

    /** 사용자 리스트 조회 (왼쪽 Grid) */
    @GetMapping("/user")
    @ResponseBody
    public List<MenuPermissionUserVO> getUserList(
            @RequestParam String companyCode,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) String position
    ) {
        return service.getUserList(companyCode, userName, dept, position);
    }

    /** 메뉴트리 조회 (오른쪽 트리) */
    @GetMapping("/menu")
    @ResponseBody
    public List<MenuTreeVO> getMenuTree(
            @RequestParam String companyCode,
            @RequestParam String userId,
            @RequestParam String menuGroup
    ) {
        return service.getMenuTree(companyCode, userId, menuGroup);
    }

    /** 권한 저장 */
    @PostMapping("/save")
    @ResponseBody
    public String saveMenuAuth(
            @RequestParam String companyCode,
            @RequestParam String userId,
            @RequestBody List<MenuAuthSaveVO> authList,
            HttpSession session
    ) {
        String updatedBy = (String) session.getAttribute("LOGIN_USER_ID");
        if (updatedBy == null) updatedBy = "SYSTEM";

        service.saveUserAuth(companyCode, userId, authList, updatedBy);

        return "SUCCESS";
    }
}
