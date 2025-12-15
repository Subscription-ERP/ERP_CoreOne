package com.rootcore.sb.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.vo.ContractVO;
import com.rootcore.sb.vo.OrderVO;
import com.rootcore.sb.vo.PaymentReadyResponseVO;
import com.rootcore.sb.vo.PlanVO;
import com.rootcore.sb.vo.TossBillingConfirmRequestVO;
import com.rootcore.sb.vo.TossConfirmResponseVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentRestController {

	private final PaymentService paymentService;

	/**
	 * 1. 결제 요청 (결제 준비) - 클라이언트가 상품명/금액 등을 보내면 orderId를 만들고 DB에 저장한 뒤, Toss 위젯에 넘겨줄
	 * 값들을 응답합니다.
	 */
	@GetMapping("/request")
	public ResponseEntity<PaymentReadyResponseVO> insertOrder(HttpSession session) {
		
		ContractVO contract = (ContractVO) session.getAttribute("contract");
		PlanVO plan = (PlanVO) session.getAttribute("plan");
		
		OrderVO ordervo = new OrderVO();
		ordervo.setOrderAmount(contract.getTotalPrice().longValue());
		ordervo.setOrderName(plan.getPlanName());

		
		PaymentReadyResponseVO responseDto = paymentService.insertOrder(ordervo, plan);
		return ResponseEntity.ok(responseDto);
	}
	
	@PostMapping("/retry")
	public ResponseEntity<TossConfirmResponseVO> retryBilling(
	        @RequestBody TossBillingConfirmRequestVO req) {

	    TossConfirmResponseVO res =
	        paymentService.retryBillingPayment(req.getSubCode());

	    return ResponseEntity.ok(res);
	}

	

}
