package com.rootcore.cm.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CmController {
	
	@GetMapping("/")
	public String main() {
		return "main/main";
	}
	
	@GetMapping("/cm/authManage")
	public String authManage() {
		return "cm/authManage";
	}
	
	@GetMapping("/cm/itemManage")
	public String itemManage() {
		return "cm/itemManage";
	}
	
	@GetMapping("/cm/deptCodeManage")
	public String deptCodeManage() {
		return "cm/deptCodeManage";
	}
	
}
