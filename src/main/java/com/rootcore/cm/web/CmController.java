package com.rootcore.cm.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CmController {

	@GetMapping("/")
	public String main(Model model) {
		model.addAttribute("page", "main"); //변수 = page, 값 = main
		return "main/main";	//템플릿경로(파일경로)
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

}
