package com.rootcore.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	// 상준_251125_login화면
	@GetMapping("/auth/login")
	public String login() {
		return "auth/login";
	}
}
