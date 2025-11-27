package com.rootcore.sb.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.rootcore.sb.domain.Payment;

@Mapper
public interface PaymentMapper {

    void insertPayment(Payment payment); // 결제 승인 시 INSERT

    // 필요하다면 이 정도만
    Payment selectLatestByOrderId(String orderId);
}