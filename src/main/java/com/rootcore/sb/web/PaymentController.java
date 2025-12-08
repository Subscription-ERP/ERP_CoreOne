package com.rootcore.sb.web;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.vo.CompanyVO;
import com.rootcore.sb.vo.ContractVO;
import com.rootcore.sb.vo.OrderVO;
import com.rootcore.sb.vo.PaymentReadyResponseVO;
import com.rootcore.sb.vo.PlanVO;
import com.rootcore.sb.vo.TossConfirmRequestVO;
import com.rootcore.sb.vo.TossConfirmResponseVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private final PaymentService paymentService;

	/**
	 * 1. 결제 요청 (결제 준비) - 클라이언트가 상품명/금액 등을 보내면 orderId를 만들고 DB에 저장한 뒤, Toss 위젯에 넘겨줄
	 * 값들을 응답합니다.
	 */
	@GetMapping("/request")
	public ResponseEntity<PaymentReadyResponseVO> insertOrder(HttpSession session) {
		OrderVO ordervo = new OrderVO();
		ContractVO contract = (ContractVO) session.getAttribute("contract");
		PlanVO plan = (PlanVO) session.getAttribute("plan");
		ordervo.setOrderAmount(contract.getTotalPrice().longValue());
		ordervo.setOrderName(plan.getPlanName());

		PaymentReadyResponseVO responseDto = paymentService.insertOrder(ordervo, plan);
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * (선택) 3. successUrl, failUrl에서 보여줄 페이지가 필요하면 이런 식으로 구현 가능 - 여기서는 단순히 메시지를 리턴하는
	 * 예제입니다.
	 */
	@GetMapping("/success")
	public ModelAndView paymentSuccess(@RequestParam String paymentKey, @RequestParam String orderId,
			@RequestParam Long amount, HttpSession session, Model model) {
		ModelAndView modelAndView = new ModelAndView("sb/success");

		// 1) Pay Confirm Request 생성
		TossConfirmRequestVO req = new TossConfirmRequestVO();
		req.setPaymentKey(paymentKey);
		req.setOrderId(orderId);
		req.setAmount(amount);

		CompanyVO company = (CompanyVO) session.getAttribute("company");
		PlanVO plan = (PlanVO) session.getAttribute("plan");
		ContractVO contract = (ContractVO) session.getAttribute("contract");

		// 2) 결제 승인 API 호출
		TossConfirmResponseVO res = paymentService.confirmPayment(req, company, plan, contract);

		// 3) 사용자에게 보여줄 데이터 모델에 담기
		model.addAttribute("payment", res);

		// 4) 결제완료 화면 렌더링
		return modelAndView;// resources/templates/payments/success.html

	}

	@GetMapping("/fail")
	public String paymentFail(@RequestParam String code, @RequestParam String message, @RequestParam String orderId,
			Model model) {
		model.addAttribute("code", code);
		model.addAttribute("message", message);
		return "sb/fail";
	}

}
