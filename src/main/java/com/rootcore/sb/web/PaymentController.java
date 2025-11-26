package com.rootcore.sb.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rootcore.sb.dto.PaymentReadyResponseDto;
import com.rootcore.sb.dto.PaymentRequestDto;
import com.rootcore.sb.dto.TossConfirmRequestDto;
import com.rootcore.sb.dto.TossConfirmResponseDto;
import com.rootcore.sb.service.PaymentService;

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
    public PaymentReadyResponseDto request(@RequestBody PaymentRequestDto requestDto) {
        // 일단 서비스 호출해서 DTO만 만들어서 리턴 (DB 오류 나면 서비스 안에서 try-catch)
        return paymentService.createPayment(requestDto);
    }
    

    /**
     * 2. 결제 승인
     *    - Toss 결제 완료 후 successUrl에서 받은 paymentKey, orderId, amount를
     *      프론트가 서버로 보내면, 서버가 토스 승인 API를 호출합니다.
     */
    @PostMapping("/confirm")
    public ResponseEntity<TossConfirmResponseDto> confirmPayment(
            @RequestBody TossConfirmRequestDto confirmRequestDto
    ) {
        log.info("Toss payment confirm request: {}", confirmRequestDto.getOrderId());
        TossConfirmResponseDto responseDto = paymentService.confirmPayment(confirmRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * (선택) 3. successUrl, failUrl에서 보여줄 페이지가 필요하면 이런 식으로 구현 가능
     *  - 여기서는 단순히 메시지를 리턴하는 예제입니다.
     */
    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {
        // 보통은 여기서 바로 server-side에서 confirm을 호출하기도 함.
        // 지금 구조는 success 페이지에서 Ajax로 /api/payments/confirm 호출하는 방식
        return ResponseEntity.ok("결제가 성공적으로 완료되었습니다. orderId=" + orderId);
    }

    @GetMapping("/fail")
    public ResponseEntity<String> paymentFail(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId
    ) {
        return ResponseEntity.badRequest().body("결제 실패: " + message);
    }
}
