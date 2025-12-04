package com.rootcore.sb.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/company")
@RequiredArgsConstructor
public class SubController {

	/**
	 * 관리자 - 회사 목록 & 구독 이력 관리 화면 예: templates/admin/companyManage.html 로 매핑
	 */
	@GetMapping("/manage")
	public String companyManagePage() {
		return "admin/companyManage"; // 나중에 만들 타임리프 템플릿 경로
	}

}
