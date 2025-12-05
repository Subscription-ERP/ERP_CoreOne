package com.rootcore.auth.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.MenuAuthSaveVO;
import com.rootcore.auth.vo.MenuPermissionUserVO;
import com.rootcore.auth.vo.MenuTreeVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/menu_permission/api")
public class MenuPermissionController {

    private final MenuPermissionService service;

    // 1) 사용자 검색 조회 
    @GetMapping("/user")
    public List<MenuPermissionUserVO> getUserList(
            HttpSession session,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) String position
    ) {
        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        return service.getUserList(companyCode, userName, dept, position);
    }


    // 2) 선택한 사용자 메뉴 권한 조회
    @GetMapping("/menu")
    public List<MenuTreeVO> getUserMenuAuth(
            @RequestParam String companyCode,
            @RequestParam String userId
    ) {
        return service.getUserMenuTree(companyCode, userId);
    }

    // 3) 권한 저장 
    @PostMapping("/save")
    public int saveMenuAuth(@RequestBody List<MenuAuthSaveVO> authList) {
        return service.saveMenuAuth(authList);
    }
}
