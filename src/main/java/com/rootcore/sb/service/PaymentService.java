package com.rootcore.sb.service;

import com.rootcore.sb.vo.PaymentConfirmRequest;
import com.rootcore.sb.vo.PaymentResultVO;

public interface PaymentService {
    PaymentResultVO confirmPayment(PaymentConfirmRequest request);
}