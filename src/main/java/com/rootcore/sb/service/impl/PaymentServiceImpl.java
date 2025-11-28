package com.rootcore.sb.service.impl;

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
import com.rootcore.sb.vo.PlanSelectRequestVO;
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
	public PaymentReadyResponseVO insertOrder(OrderVO requestVO) {

		// 주문번호 생성 (예: UUID 사용, 실제로는 규칙 정해서 사용)

		// ✅ 주문 정보 ORDER 테이블에 저장
		OrderVO order = new OrderVO();
		order.setOrderName(requestVO.getOrderName());
		order.setOrderAmount(requestVO.getAmount());
		order.setOrderStatus("READY"); // 주문 상태
		order.setOrderType("NORMAL"); // 필요시 상수/enum 처리
		order.setCreateDate(LocalDateTime.now());
		order.setUpdateDate(LocalDateTime.now());
		order.setCreatedBy("SYSTEM"); // 나중에 로그인 사용자로 교체
		order.setUpdatedBy("SYSTEM");

		orderMapper.insertOrder(order);

		// 프론트에서 Toss 위젯 호출할 때 필요한 값들 내려줌
		PaymentReadyResponseVO responseVO = new PaymentReadyResponseVO();
		responseVO.setOrderId(order.getOrderId());
		responseVO.setOrderName(requestVO.getOrderName());
		responseVO.setAmount(requestVO.getAmount());

		responseVO.setSuccessUrl("http://localhost:8080/api/payments/success"); // 예시
		responseVO.setFailUrl("http://localhost:8080/api/payments/fail"); // 예시

		return responseVO;
	}

	@Override
	public TossConfirmResponseVO confirmPayment(TossConfirmRequestVO requestVO,

			CompanyVO company, PlanSelectRequestVO plan, ContractVO contract) {

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
		companyMapper.insertCompany(company);
		
		//계약서 등록
		contractMapper.insertContract(contract);
		// 3. PAYMENT 테이블에 결제 이력 INSERT
		PaymentVO payment = new PaymentVO();
		// payment 객체생성
		payment.setOrderId(order.getOrderId());
		payment.setTotalPrice(tossResponse.getTotalAmount()); // TOTAL_PRICE
		payment.setPaymentStat(tossResponse.getStatus()); // PAYMENT_STAT (SUCCESS 등)
		payment.setPaymentKey(tossResponse.getPaymentKey()); // PAYMENT_KEY
		payment.setPaymentDate(tossResponse.getApprovedAt().toLocalDateTime()); // PAYMENT_DATE
		payment.setCreatedBy("SYSTEM");
		payment.setCreateDate(LocalDateTime.now());
		payment.setUpdatedBy("SYSTEM");
		payment.setUpdateDate(LocalDateTime.now());
		payment.setCompanyCode("COM25112700001");
		// 고정데이터로 들어감
		payment.setSubCode("SUB25112700001");
		// payment객체안에 값들을 채워넣음
//		paymentMapper.insertPayment(payment);
		// 셋팅된 payment객체를 mapper로 전달

		// ORDER 업데이트
		orderMapper.updateOrderSubCode(payment.getOrderId(), payment.getSubCode(), "SYSTEM");

		// 4. ORDER 테이블의 주문 상태 업데이트 (예: PAID / SUCCESS)
		orderMapper.updateOrderStatus(order.getOrderId(), "SUCCESS");

		// 5. 그대로 Toss 응답 반환 (프론트가 필요로 하는 경우)
		return tossResponse;

	}
}
