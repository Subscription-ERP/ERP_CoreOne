package com.rootcore.sb.service;

import com.rootcore.sb.dto.PaymentReadyResponseDto;
import com.rootcore.sb.dto.PaymentRequestDto;
import com.rootcore.sb.dto.TossConfirmRequestDto;
import com.rootcore.sb.dto.TossConfirmResponseDto;

public interface PaymentService {
	   PaymentReadyResponseDto createPayment(PaymentRequestDto requestDto);

	    TossConfirmResponseDto confirmPayment(TossConfirmRequestDto requestDto);
}