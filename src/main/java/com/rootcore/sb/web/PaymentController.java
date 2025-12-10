package com.rootcore.sb.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.vo.CompanyVO;
import com.rootcore.sb.vo.ContractVO;
import com.rootcore.sb.vo.OrderVO;
import com.rootcore.sb.vo.PlanVO;
import com.rootcore.sb.vo.TossConfirmRequestVO;
import com.rootcore.sb.vo.TossConfirmResponseVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class PaymentController {
	
	private final PaymentService paymentService;
	/**
	 * (선택) 3. successUrl, failUrl에서 보여줄 페이지가 필요하면 이런 식으로 구현 가능 - 여기서는 단순히 메시지를 리턴하는
	 * 예제입니다.
	 */
	@GetMapping("/success")
	public String paymentSuccess(@RequestParam String paymentKey, @RequestParam String orderId,
			@RequestParam Long amount, HttpSession session, Model model) {
		OrderVO order = new OrderVO();

		// 1) Pay Confirm Request 생성
		TossConfirmRequestVO req = new TossConfirmRequestVO();
		req.setPaymentKey(paymentKey);
		req.setOrderId(orderId);
		req.setAmount(amount);

		CompanyVO company = (CompanyVO) session.getAttribute("company");
		PlanVO plan = (PlanVO) session.getAttribute("plan");
		ContractVO contract = (ContractVO) session.getAttribute("contract");
		order.setOrderAmount(contract.getTotalPrice().longValue());
		order.setOrderName(plan.getPlanName());

		// 2) 결제 승인 API 호출
		TossConfirmResponseVO res = paymentService.confirmPayment(req, company, plan, contract);

		// 3) 사용자에게 보여줄 데이터 모델에 담기
		model.addAttribute("payment", res);

		// 4) 결제완료 화면 렌더링
		return "sb/success";// resources/templates/payments/success.html

	}

	@GetMapping("/billing/success") 
	public String billingSuccess(@RequestParam String authKey,
	                                   @RequestParam String customerKey,
	                                   HttpSession session, Model model) {
	    
	    CompanyVO company = (CompanyVO) session.getAttribute("company");
	    PlanVO plan = (PlanVO) session.getAttribute("plan");
	    ContractVO contract = (ContractVO) session.getAttribute("contract");

	    // authKey로 billingKey 발급하고, 구독/결제/계약/회사까지 한 번에 처리
	    TossConfirmResponseVO res = paymentService.createSubscriptionWithBillingKey(
	            authKey, customerKey, company, plan, contract);

	    model.addAttribute("billingKey", res.getBillingKey()); // 응답 VO에 넣어두면 화면에서 볼 수 있음(선택)
	    return "sb/billing";
	}


	@GetMapping("/fail")
	public String paymentFail(@RequestParam String code, @RequestParam String message, @RequestParam String orderId,
			Model model) {
		model.addAttribute("code", code);
		model.addAttribute("message", message);
		return "sb/fail";
	}
}
