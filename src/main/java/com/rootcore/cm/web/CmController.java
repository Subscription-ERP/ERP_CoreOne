package com.rootcore.cm.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class CmController {

	@GetMapping("/")
	public String main(Model model, HttpSession session) {
		if(session.getAttribute("LOGIN_COMPANY_CODE") == null) {
			model.addAttribute("page", "main"); //변수 = page, 값 = main
			return "main/main";	//템플릿경로(파일경로)
		} else {
			return "redirect:/cm/dashBoard";
		}
	}

	@GetMapping("/cm/authManage")
	public String authManage() {
		return "cm/authManage";
	}

	@GetMapping("/cm/skuManage")
	public String skuManage() {
		return "cm/skuManage";
	}
	
	// 부서코드관리 페이지
	@GetMapping("/cm/deptCodeManage")
	public String deptCodeManage() {
		return "cm/deptCodeManage";
	}
	
	@GetMapping("/cm/dashBoard")
	public String dashBoard() {
		return "cm/dashBoard";
	}

}
