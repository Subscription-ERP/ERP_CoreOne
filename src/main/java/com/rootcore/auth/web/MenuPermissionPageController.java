package com.rootcore.auth.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MenuPermissionPageController {

    /** 메뉴 권한 화면 이동 */
    @GetMapping("/auth/menu_permission")
    public String menuPermissionPage(Model model) {
    	model.addAttribute("breadcrumb", List.of(
    	        "시스템관리",
    	        "메뉴별 권한관리"
    	    ));
        return "auth/menu_permission";   // templates/auth/menu_permission.html
    }
}
