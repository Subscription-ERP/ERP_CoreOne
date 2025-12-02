package com.rootcore.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")  
public class MenuPermissionPageController {

    @GetMapping("/menu_permission")   // 브라우저에서 접근할 URL
    public String menuPermissionPage() {
        return "auth/menu_permission";  // templates/auth/menu_permission.html
    }
}
