package com.rootcore.auth.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MenuPermissionPageController {

    /** 메뉴 권한 화면 이동 */
    @GetMapping("/auth/menu_permission")
    public String menuPermissionPage() {
        return "auth/menu_permission";   // templates/auth/menu_permission.html
    }
}
