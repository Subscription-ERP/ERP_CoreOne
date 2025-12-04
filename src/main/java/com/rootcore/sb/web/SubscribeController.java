package com.rootcore.sb.web;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.vo.PaymentVO;
import com.rootcore.sb.vo.PlanVO;
import com.rootcore.sb.vo.SubscribeVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subscribe")
@RequiredArgsConstructor
public class SubscribeController {

	private final PaymentService paymentService;

	@GetMapping("/sb/Manage")
	public String getInactiveHistory(Model model, HttpSession session) {
		// 1) 로그인한 회사정보
		String comCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");

		List<SubscribeVO> historyList = paymentService.selectInactiveSubListByComCode(comCode);
		return "sb/sbManage";

	}

}
