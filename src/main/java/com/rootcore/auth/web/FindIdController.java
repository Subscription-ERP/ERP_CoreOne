package com.rootcore.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// id찾기 

@Controller
public class FindIdController {

    @GetMapping("/auth/find_id")
    public String findIdPage() {
        return "auth/find_id";   // templates/auth/find_id.html
    }
}
