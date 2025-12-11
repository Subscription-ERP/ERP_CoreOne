package com.rootcore.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthViewController {

    @GetMapping("/noAuth")
    public String noAuthPage() {
        // templates/auth/noAuth.html
        return "auth/noAuth";
    }
}
