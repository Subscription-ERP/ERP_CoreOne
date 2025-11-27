package com.rootcore.sb.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.vo.PaymentReadyResponseVO;
import com.rootcore.sb.vo.PaymentRequestVO;
import com.rootcore.sb.vo.TossConfirmRequestVO;
import com.rootcore.sb.vo.TossConfirmResponseVO;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * 1. 결제 요청 (결제 준비)
     *    - 클라이언트가 상품명/금액 등을 보내면 orderId를 만들고 DB에 저장한 뒤,
     *      Toss 위젯에 넘겨줄 값들을 응답합니다.
     */
//    @PostMapping("/request")
//    public ResponseEntity<PaymentReadyResponseDto> createPayment(
//            @RequestBody PaymentRequestDto requestDto
//    ) {
//        PaymentReadyResponseDto responseDto = paymentService.createPayment(requestDto);
//        return ResponseEntity.ok(responseDto);
//    }
    @PostMapping("/request")
    public PaymentReadyResponseVO request(@RequestBody PaymentRequestVO requestVO) {
        // 일단 서비스 호출해서 DTO만 만들어서 리턴 (DB 오류 나면 서비스 안에서 try-catch)
        return paymentService.createPayment(requestVO);
    }
    

    /**
     * 2. 결제 승인
     *    - Toss 결제 완료 후 successUrl에서 받은 paymentKey, orderId, amount를
     *      프론트가 서버로 보내면, 서버가 토스 승인 API를 호출합니다.
     */
    @PostMapping("/confirm")
    public ResponseEntity<TossConfirmResponseVO> confirmPayment(
            @RequestBody TossConfirmRequestVO confirmRequestVO
    ) {
        log.info("Toss payment confirm request: {}", confirmRequestVO.getOrderId());
        TossConfirmResponseVO responseVO = paymentService.confirmPayment(confirmRequestVO);
        return ResponseEntity.ok(responseVO);
    }

    /**
     * (선택) 3. successUrl, failUrl에서 보여줄 페이지가 필요하면 이런 식으로 구현 가능
     *  - 여기서는 단순히 메시지를 리턴하는 예제입니다.
     */
    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount,
            Model model
    ) {
        // 1) Pay Confirm Request 생성
        TossConfirmRequestVO req = new TossConfirmRequestVO();
        req.setPaymentKey(paymentKey);
        req.setOrderId(orderId);
        req.setAmount(amount);

        // 2) 결제 승인 API 호출
        TossConfirmResponseVO res = paymentService.confirmPayment(req);

        // 3) 사용자에게 보여줄 데이터 모델에 담기
        model.addAttribute("payment", res);

        // 4) 결제완료 화면 렌더링
        return "sb/success"; // resources/templates/payments/success.html
    }

    @GetMapping("/fail")
    public String paymentFail(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId,
            Model model
    ) {
    	   model.addAttribute("code", code);
    	    model.addAttribute("message", message);
    	    return "sb/fail";
    }
}
