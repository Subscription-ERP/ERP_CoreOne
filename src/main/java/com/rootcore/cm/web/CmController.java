package com.rootcore.cm.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CmController {
	
	@GetMapping("/")
	public String main() {
		return "main/main";
	}
}
