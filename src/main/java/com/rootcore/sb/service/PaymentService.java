package com.rootcore.sb.service;

import com.rootcore.sb.vo.PaymentReadyResponseVO;
import com.rootcore.sb.vo.PaymentRequestVO;
import com.rootcore.sb.vo.TossConfirmRequestVO;
import com.rootcore.sb.vo.TossConfirmResponseVO;

public interface PaymentService {
	   PaymentReadyResponseVO createPayment(PaymentRequestVO requestVO);
//결제를 시작할 때 필요한 기능
	    TossConfirmResponseVO confirmPayment(TossConfirmRequestVO requestVO);
//결제가 완료된 후 승인하는 기능
}