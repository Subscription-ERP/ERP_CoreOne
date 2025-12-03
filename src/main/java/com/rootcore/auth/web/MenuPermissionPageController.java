package com.rootcore.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class MenuPermissionPageController {

    @GetMapping("/menu_permission")
    public String menuPermissionPage() {
        return "auth/menu_permission";
    }
}
