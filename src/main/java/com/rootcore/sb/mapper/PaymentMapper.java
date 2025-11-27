package com.rootcore.sb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.sb.domain.Payment;

@Mapper
public interface PaymentMapper {

    void insertPayment(Payment payment);

    Payment selectByOrderId(@Param("orderId") String orderId);

    void updatePaymentStatus(
            @Param("orderId") String orderId,
            @Param("status") String status,
            @Param("paymentKey") String paymentKey
    );
}