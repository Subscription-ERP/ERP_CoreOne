package com.rootcore.sb.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.vo.PaymentConfirmRequest;
import com.rootcore.sb.vo.PaymentResultVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WidgetController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PaymentService paymentService;

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    @ResponseBody 	// JSON 응답
    public ResponseEntity<PaymentResultVO> confirmPayment(@RequestBody PaymentConfirmRequest request) {

        logger.info("결제 승인 요청: paymentKey={}, orderId={}, amount={}",
                request.getPaymentKey(), request.getOrderId(), request.getAmount());

        PaymentResultVO result = paymentService.confirmPayment(request);

        return ResponseEntity.ok(result);
    }
}
