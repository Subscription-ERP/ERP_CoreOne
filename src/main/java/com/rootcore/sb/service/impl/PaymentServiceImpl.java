package com.rootcore.sb.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.rootcore.sb.client.TossPaymentClient;
import com.rootcore.sb.mapper.CompanyMapper;
import com.rootcore.sb.mapper.ContractMapper;
import com.rootcore.sb.mapper.OrderMapper;
import com.rootcore.sb.mapper.PaymentMapper;
import com.rootcore.sb.mapper.SubscribeMapper;
import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.vo.CompanyVO;
import com.rootcore.sb.vo.ContractVO;
import com.rootcore.sb.vo.OrderVO;
import com.rootcore.sb.vo.PaymentReadyResponseVO;
import com.rootcore.sb.vo.PaymentVO;
import com.rootcore.sb.vo.PlanVO;
import com.rootcore.sb.vo.SubscribeVO;
import com.rootcore.sb.vo.TossConfirmRequestVO;
import com.rootcore.sb.vo.TossConfirmResponseVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

	private final OrderMapper orderMapper; // 주문
	private final PaymentMapper paymentMapper; // 결제이력
	private final ContractMapper contractMapper; // 계약서
	
	
	private final SubscribeMapper subscribeMapper; // 구독
	private final CompanyMapper companyMapper; // 회사 (상태 변경 정도용)
	private final TossPaymentClient tossPaymentClient;



	/**
	 * 회사 등록이 끝난 상태에서: 회사코드 + 플랜정보 + 금액을 가지고 주문을 생성하고 프론트에서 Toss 위젯을 띄울 수 있도록 값 반환
	 */
	@Override
	public PaymentReadyResponseVO insertOrder(OrderVO ordervo, PlanVO plan) {

		// 주문번호 생성 (예: UUID 사용, 실제로는 규칙 정해서 사용)

		// ✅ 주문 정보 ORDER 테이블에 저장
		ordervo.setOrderName(ordervo.getOrderName());
		ordervo.setOrderAmount(ordervo.getOrderAmount());
		ordervo.setOrderStatus("READY"); // 주문 상태
		ordervo.setOrderType("NORMAL"); // 필요시 상수/enum 처리
		ordervo.setCreateDate(LocalDateTime.now());
		ordervo.setUpdateDate(LocalDateTime.now());
		ordervo.setCreatedBy("SYSTEM"); // 나중에 로그인 사용자로 교체
		ordervo.setUpdatedBy("SYSTEM");
		ordervo.setCompanyCode("0000");
		ordervo.setPlanCode(plan.getPlanCode());

		orderMapper.insertOrder(ordervo);

		// 프론트에서 Toss 위젯 호출할 때 필요한 값들 내려줌
		PaymentReadyResponseVO responseVO = new PaymentReadyResponseVO();
		responseVO.setOrderId(ordervo.getOrderId());
		responseVO.setOrderName(ordervo.getOrderName());
		responseVO.setAmount(ordervo.getOrderAmount());

		responseVO.setSuccessUrl("http://localhost:8080/api/payments/success"); // 예시
		responseVO.setFailUrl("http://localhost:8080/api/payments/fail"); // 예시

		return responseVO;
	}

	@Override
	public TossConfirmResponseVO confirmPayment(TossConfirmRequestVO requestVO,

			CompanyVO company, PlanVO plan, ContractVO contract) {

		// 1. ORDER 테이블에서 주문 조회 및 금액 검증
		OrderVO order = orderMapper.selectByOrderId(requestVO.getOrderId());
		if (order == null) {
			throw new IllegalArgumentException("존재하지 않는 주문입니다.");
		}

		if (!order.getOrderAmount().equals(requestVO.getAmount())) {
			throw new IllegalArgumentException("금액이 일치하지 않습니다.");
		}

		System.out.println(requestVO);
		// 2. 토스 결제 승인 API 호출
		TossConfirmResponseVO tossResponse = tossPaymentClient.confirmPayment(requestVO);

		//회사등록
        company.setCreatedBy("SYSTEM");
        company.setCreateDate(LocalDateTime.now());
        company.setUpdatedBy("SYSTEM");
        company.setUpdateDate(LocalDateTime.now());
		companyMapper.insertCompany(company);	//세션정보를 불러와 insert 매퍼실행
		
		//계약서 등록
		
		contract.setCompanyCode(company.getCompanyCode());
		contract.setPlanCode(plan.getPlanCode());
		contract.setCreatedBy("SYSTEM");
		contract.setCreateDate(LocalDateTime.now());
		contract.setUpdatedBy("SYSTEM");
		contract.setUpdateDate(LocalDateTime.now());
		contractMapper.insertContract(contract);
		
		//구독생성
		 SubscribeVO subscribe = new SubscribeVO();
		subscribe.setCompanyCode(company.getCompanyCode());
		subscribe.setSubsStatus("ACTIVE");
		subscribe.setSubsStart(LocalDate.now());
		subscribe.setSubsEnd(LocalDate.now().plusMonths(contract.getSubsPeriod()));
		subscribe.setCreatedBy("SYSTEM");
		subscribe.setCreateDate(LocalDateTime.now());
		subscribe.setUpdatedBy("SYSTEM");
		subscribe.setUpdateDate(LocalDateTime.now());
		subscribe.setPlanCode(plan.getPlanCode());
		subscribe.setContractCode(contract.getContractCode());
		//subscribe.setBillingPeriod();
		subscribe.setCurrentUserCount(contract.getUserCount());
		subscribe.setCurrentPrice(contract.getTotalPrice().doubleValue());
	
		subscribeMapper.insertSubscribe(subscribe);
		
		// 3. PAYMENT 테이블에 결제 이력 INSERT
		// payment 객체생성
		 PaymentVO payment = new PaymentVO();
		payment.setOrderId(order.getOrderId());
		payment.setTotalPrice(tossResponse.getTotalAmount()); // TOTAL_PRICE
		payment.setPaymentStat(tossResponse.getStatus()); // PAYMENT_STAT (SUCCESS 등)
		payment.setPaymentKey(tossResponse.getPaymentKey()); // PAYMENT_KEY
		payment.setPaymentDate(tossResponse.getApprovedAt().toLocalDateTime()); // PAYMENT_DATE
		payment.setCreatedBy("SYSTEM");
		payment.setCreateDate(LocalDateTime.now());
		payment.setUpdatedBy("SYSTEM");
		payment.setUpdateDate(LocalDateTime.now());
		payment.setCompanyCode(company.getCompanyCode());
		// 고정데이터로 들어감
		payment.setSubCode(subscribe.getSubCode());
		// payment객체안에 값들을 채워넣음
//		paymentMapper.insertPayment(payment);
		// 셋팅된 payment객체를 mapper로 전달
		paymentMapper.insertPayment(payment);

		// ORDER 업데이트
		orderMapper.updateOrderSubCode(payment.getOrderId(), payment.getSubCode(), "SYSTEM");
		orderMapper.updateOrderCompanyCode(payment.getOrderId(), company.getCompanyCode(), "SYSTEM");

		// 4. ORDER 테이블의 주문 상태 업데이트 (예: PAID / SUCCESS)
		
		orderMapper.updateOrderStatus(order.getOrderId(), "SUCCESS");

		// 5. 그대로 Toss 응답 반환 (프론트가 필요로 하는 경우)
		return tossResponse;

	}


	
}
