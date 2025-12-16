package com.rootcore.sb.service;

import java.util.List;
import java.util.Map;

import com.rootcore.sb.vo.CompanyVO;
import com.rootcore.sb.vo.ContractVO;
import com.rootcore.sb.vo.OrderVO;
import com.rootcore.sb.vo.PaymentReadyResponseVO;
import com.rootcore.sb.vo.PaymentVO;
import com.rootcore.sb.vo.PlanVO;
import com.rootcore.sb.vo.SubscribeVO;
import com.rootcore.sb.vo.TossBillingConfirmRequestVO;
import com.rootcore.sb.vo.TossConfirmRequestVO;
import com.rootcore.sb.vo.TossConfirmResponseVO;

import jakarta.servlet.http.HttpSession;

public interface PaymentService {
	PaymentReadyResponseVO insertOrder(OrderVO ordervo, PlanVO plan);

	// 1) 일반결제 (일회성 / 구독 첫 결제)
	TossConfirmResponseVO confirmPayment(TossConfirmRequestVO requestVO, CompanyVO company, PlanVO plan,
			ContractVO contract,  HttpSession session);

	// 2) 정기결제 - 카드 등록 성공 후 구독 생성
	TossConfirmResponseVO createSubscriptionWithBillingKey(String authKey, String customerKey, CompanyVO company,
			PlanVO plan, ContractVO contract);

	// 3) 정기결제 - 특정 구독 1건에 대해 결제 1번 실행
	/* 서비스 로직은 매퍼인터페이스 선언 X */
	TossConfirmResponseVO chargeSubscription(TossBillingConfirmRequestVO req);

	Map<String, String> createCompanyManagerAccount(CompanyVO company);

	// 회사별 비활성 구독 이력 조회
	List<SubscribeVO> selectInactiveSubListByComCode(String companyCode);

	SubscribeVO selectSubDetail(String companyCode);

	List<PaymentVO> selectPaymentHistory(String companyCode);

	
}
