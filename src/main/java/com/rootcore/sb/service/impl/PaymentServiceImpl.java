package com.rootcore.sb.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.sb.client.TossPaymentClient;
import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.vo.PaymentConfirmRequest;
import com.rootcore.sb.vo.PaymentResultVO;
import com.rootcore.sb.vo.TossConfirmRequest;
import com.rootcore.sb.vo.TossConfirmResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final TossPaymentClient tossPaymentClient;
    // private final OrderRepository orderRepository; // 주문 검증용 (나중에 붙이면 됨)

    @Override
    @Transactional
    public PaymentResultVO confirmPayment(PaymentConfirmRequest request) {

        // 1. 주문/금액 검증 (DB와 비교하는 게 원칙)
        // Order order = orderRepository.findByOrderId(request.getOrderId());
        // if (!order.getAmount().equals(request.getAmount())) throw new IllegalStateException("금액 불일치");

        // 2. 토스 승인 요청 DTO 생성
        TossConfirmRequest tossRequest = TossConfirmRequest.builder()
                .paymentKey(request.getPaymentKey())
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .build();

        // 3. 토스 결제 승인 API 호출
        TossConfirmResponse response = tossPaymentClient.confirmPayment(tossRequest);

        boolean success = "DONE".equalsIgnoreCase(response.getStatus());

        // 4. DB에 결제결과 반영 (성공/실패 상태 변경)
        // if (success) order.markPaid(response.getMethod(), response.getPaymentKey());
        // else order.markPaymentFailed();
        // orderRepository.save(order);

        // 5. 프론트로 보낼 DTO 생성
        return PaymentResultVO.builder()
                .success(success)
                .orderId(response.getOrderId())
                .paymentKey(response.getPaymentKey())
                .amount(response.getTotalAmount())
                .method(response.getMethod())
                .status(response.getStatus())
                .message(success ? "결제 성공" : "결제 실패")
                .build();
    }
}
