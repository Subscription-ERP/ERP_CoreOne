package com.rootcore.sb.mapper;

import com.rootcore.sb.vo.PaymentConfirmRequest;
import com.rootcore.sb.vo.PaymentResultVO;

public interface PaymentMapper {
    PaymentResultVO confirmPayment(PaymentConfirmRequest request);
}
