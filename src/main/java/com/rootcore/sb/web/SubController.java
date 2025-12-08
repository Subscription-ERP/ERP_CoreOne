package com.rootcore.sb.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.vo.PaymentVO;
import com.rootcore.sb.vo.PlanVO;
import com.rootcore.sb.vo.SubscribeVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/company")
@RequiredArgsConstructor
public class SubController {

	private final PaymentService paymentService;
	
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
	    public String UserManagePage(HttpSession session, Model model) {
	        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
	        System.out.println("로그인한 회사코드" + companyCode);

//	        if (companyCode == null) {
//	            throw new RuntimeException("로그인 정보가 없습니다.");
//	        }

	        // ★ 타임리프에서 사용할 상세 정보
	        SubscribeVO subDetail = paymentService.selectSubDetail(companyCode);
	        
	        model.addAttribute("subDetail", subDetail);

	        return "sb/sbUserManage"; // 타임리프 HTML 렌더링
	    }
}
