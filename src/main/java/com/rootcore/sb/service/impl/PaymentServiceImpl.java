package com.rootcore.sb.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.sb.client.TossPaymentClient;
import com.rootcore.sb.domain.Payment;
import com.rootcore.sb.dto.PaymentReadyResponseDto;
import com.rootcore.sb.dto.PaymentRequestDto;
import com.rootcore.sb.dto.TossConfirmRequestDto;
import com.rootcore.sb.dto.TossConfirmResponseDto;
import com.rootcore.sb.mapper.PaymentMapper;
import com.rootcore.sb.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final TossPaymentClient tossPaymentClient;


    public PaymentServiceImpl(
            PaymentMapper paymentMapper,
            TossPaymentClient tossPaymentClient
    ) {
        this.paymentMapper = paymentMapper;
        this.tossPaymentClient = tossPaymentClient;
    }

    @Override
    public PaymentReadyResponseDto createPayment(PaymentRequestDto requestDto) {

        // 주문번호 생성 (예: UUID 사용, 실제로는 규칙 정해서 사용)
        String orderId = "ORD-" + UUID.randomUUID();

        // 결제 정보 DB에 저장 (상태: READY)
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setOrderName(requestDto.getOrderName());
        payment.setAmount(requestDto.getAmount());
        payment.setStatus("READY");
        payment.setCustomerEmail(requestDto.getCustomerEmail());
        payment.setCustomerName(requestDto.getCustomerName());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        paymentMapper.insertPayment(payment);

        // 프론트에서 Toss 위젯 호출할 때 필요한 값들 내려줌
        PaymentReadyResponseDto responseDto = new PaymentReadyResponseDto();
        responseDto.setOrderId(orderId);
        responseDto.setOrderName(requestDto.getOrderName());
        responseDto.setAmount(requestDto.getAmount());

        return responseDto;
    }

    @Override
    public TossConfirmResponseDto confirmPayment(TossConfirmRequestDto requestDto) {

        // 1. 우리 DB에 있는 결제 정보 확인 (orderId & amount 체크)
        Payment payment = paymentMapper.selectByOrderId(requestDto.getOrderId());
        if (payment == null) {
            throw new IllegalArgumentException("존재하지 않는 주문입니다.");
        }

        if (!payment.getAmount().equals(requestDto.getAmount())) {
            throw new IllegalArgumentException("금액이 일치하지 않습니다.");
        }

        // 2. 토스 결제 승인 API 호출
        TossConfirmResponseDto tossResponse = tossPaymentClient.confirmPayment(requestDto);

        // 3. 승인 성공 시 DB 상태 업데이트
        paymentMapper.updatePaymentStatus(
                requestDto.getOrderId(),
                tossResponse.getStatus(),          // SUCCESS or 기타 상태
                tossResponse.getPaymentKey()
        );

        return tossResponse;
    }
}
