package com.rootcore.sb.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/company")
public class SubController {

	/**
	 * 관리자 - 회사 목록 & 구독 이력 관리 화면 예: templates/sb/sbManage.html 로 매핑
	 */
	@GetMapping("/manage")
	public String AdminManagePage() {
		return "sb/sbManage"; // 나중에 만들 타임리프 템플릿 경로
	}

	/**
	 * 회사(사용자) - 구독 상세 & 결제 이력 관리 화면 예: templates/sb/sbUserManage.html 로 매핑
	 */
	@GetMapping("/UserManage")
	public String UserManagePage() {
		return "sb/sbUserManage";
	}
}
