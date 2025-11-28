package com.rootcore.sb.service;

import com.rootcore.sb.vo.CompanyVO;
import com.rootcore.sb.vo.ContractVO;
import com.rootcore.sb.vo.OrderVO;
import com.rootcore.sb.vo.PaymentReadyResponseVO;
import com.rootcore.sb.vo.PlanSelectRequestVO;
import com.rootcore.sb.vo.TossConfirmRequestVO;
import com.rootcore.sb.vo.TossConfirmResponseVO;

public interface PaymentService {
	   PaymentReadyResponseVO insertOrder(OrderVO requestVO);
//결제를 시작할 때 필요한 기능
	    TossConfirmResponseVO confirmPayment(TossConfirmRequestVO requestVO,
	    									 CompanyVO company,
	    									 PlanSelectRequestVO plan,
	    									 ContractVO contract);
//결제가 완료된 후 승인하는 기능
	    // 신규 구독 + 결제 동시 처리
//	    void handleNewSubscribeAndPayment(NewSubsPaymentRequest req);
//
//	    // 기존 구독 결제만 처리
//	    void handleExistSubscribePayment(ExistSubsPaymentRequest req);
}