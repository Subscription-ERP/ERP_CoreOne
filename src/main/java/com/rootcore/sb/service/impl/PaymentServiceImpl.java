package com.rootcore.sb.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.auth.util.PasswordUtil;
import com.rootcore.sb.client.TossPaymentClient;
import com.rootcore.sb.crypto.BillingKeyCrypto;
import com.rootcore.sb.mapper.CompanyMapper;
import com.rootcore.sb.mapper.ContractMapper;
import com.rootcore.sb.mapper.OrderMapper;
import com.rootcore.sb.mapper.PaymentMapper;
import com.rootcore.sb.mapper.SubscribeMapper;
import com.rootcore.sb.mapper.UserMapper;
import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.vo.CompanyVO;
import com.rootcore.sb.vo.ContractVO;
import com.rootcore.sb.vo.OrderVO;
import com.rootcore.sb.vo.PaymentReadyResponseVO;
import com.rootcore.sb.vo.PaymentVO;
import com.rootcore.sb.vo.PlanVO;
import com.rootcore.sb.vo.SbLoginVO;
import com.rootcore.sb.vo.SbUserVO;
import com.rootcore.sb.vo.SubscribeVO;
import com.rootcore.sb.vo.TossBillingConfirmRequestVO;
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
	private final UserMapper userMapper;
	private final TossPaymentClient tossPaymentClient;
	// 🔐 빌링키 양방향 암복호화 컴포넌트
	private final BillingKeyCrypto billingKeyCrypto;
	private final PasswordEncoder passwordEncoder;

	@Value("${project.url}")
	String url;

	/**
	 * 회사 등록이 끝난 상태에서: 회사코드 + 플랜정보 + 금액을 가지고 주문을 생성하고 프론트에서 Toss 위젯을 띄울 수 있도록 값 반환
	 */
	@Override
	public PaymentReadyResponseVO insertOrder(OrderVO ordervo, PlanVO plan) {

		// 주문번호 생성 (예: UUID 사용, 실제로는 규칙 정해서 사용)

		// ✅ 주문 정보 ORDER 테이블에 저장

		ordervo.setOrderStatus("READY"); // 주문 상태
		ordervo.setOrderType("NORMAL"); // 필요시 상수/enum 처리
		ordervo.setCreateDate(LocalDateTime.now());
		ordervo.setPlanCode(plan.getPlanCode());
		ordervo.setCreatedBy("SYSTEM"); // 나중에 로그인 사용자로 교체
		ordervo.setCompanyCode("0000");
		orderMapper.insertOrder(ordervo);

		// 프론트에서 Toss 위젯 호출할 때 필요한 값들 내려줌
		PaymentReadyResponseVO responseVO = new PaymentReadyResponseVO();
		responseVO.setOrderId(ordervo.getOrderId());
		responseVO.setOrderName(ordervo.getOrderName());
		responseVO.setAmount(ordervo.getOrderAmount());

		responseVO.setSuccessUrl(url + "/api/success"); // 예시
		responseVO.setFailUrl(url + "/api/payments/fail"); // 예시

		return responseVO;
	}

	@Override
	@Transactional
	public TossConfirmResponseVO confirmPayment(TossConfirmRequestVO requestVO, CompanyVO company, PlanVO plan,
			ContractVO contract) {

		// 1. ORDER 테이블에서 주문 조회 및 금액 검증
		OrderVO order = orderMapper.selectByOrderId(requestVO.getOrderId());
		if (order == null) {
			throw new IllegalArgumentException("존재하지 않는 주문입니다.");
		}

		if (!order.getOrderAmount().equals(requestVO.getAmount())) {
			throw new IllegalArgumentException("금액이 일치하지 않습니다.");
		}

		// 2. 토스 결제 승인 API 호출
		TossConfirmResponseVO tossResponse = tossPaymentClient.confirmPayment(requestVO);
	

		// 회사등록
		CompanyVO existCompany = companyMapper.selectCompany(company.getCompanyCode());
		if (existCompany == null) {
			company.setCreatedBy("SYSTEM");
			company.setCreateDate(LocalDateTime.now());
			company.setCompanyCode("0000");
			companyMapper.insertCompany(company); // 세션정보를 불러와 insert 매퍼실행
		} else {
			company = existCompany; // 기존 회사 정보 사용
		}

		// 계약서 등록

//		contract.setCompanyCode(company.getCompanyCode());
		contract.setCompanyCode("0000");
		contract.setPlanCode(plan.getPlanCode());
		contract.setCompanyName(company.getCompanyName());
		contract.setCeoName(company.getCeoName());
		contract.setCreateDate(LocalDateTime.now());
		contract.setCreatedBy("SYSTEM");
		contract.setContractStart(LocalDateTime.now());
		contract.setContractEnd(contract.getContractStart().plusMonths(contract.getSubsPeriod()));
		contractMapper.insertContract(contract);

		// 구독생성
		LocalDate today = LocalDate.now();
		LocalDateTime now = LocalDateTime.now();
		SubscribeVO subscribe = new SubscribeVO();
//		subscribe.setCompanyCode(company.getCompanyCode());
		subscribe.setCompanyCode("0000");
		subscribe.setSubsStatus("ACTIVE");
		subscribe.setSubsStart(LocalDate.now());
		subscribe.setSubsEnd(LocalDate.now().plusMonths(contract.getSubsPeriod()));
		subscribe.setCreatedBy("SYSTEM");
		subscribe.setCreateDate(LocalDateTime.now());
		subscribe.setPlanCode(plan.getPlanCode());
		subscribe.setContractCode(contract.getContractCode());
		// subscribe.setBillingPeriod();
		subscribe.setCurrentUserCount(contract.getUserCount());
		subscribe.setCurrentPrice(contract.getTotalPrice().doubleValue());
		subscribe.setRecentBillingDate(today);
		subscribe.setNextBillingDate(null);
		subscribe.setBillingPeriod(contract.getBillingPeriod());

		subscribeMapper.insertSubscribe(subscribe);

		
		String method = tossResponse.getMethod(); // ex) "CARD", "VBANK", "BANK", ...
		// 3. PAYMENT 테이블에 결제 이력 INSERT
		// payment 객체생성
		PaymentVO payment = new PaymentVO();

		payment.setOrderId(order.getOrderId());
		payment.setSubCode(subscribe.getSubCode());
//		payment.setCompanyCode(company.getCompanyCode());
		payment.setCompanyCode("0000");
		payment.setTotalPrice(tossResponse.getTotalAmount()); // TOTAL_PRICE
		payment.setPaymentStat(tossResponse.getStatus()); // PAYMENT_STAT (SUCCESS 등)
		payment.setPaymentKey(tossResponse.getPaymentKey()); // PAYMENT_KEY
		payment.setPaymentMethod(tossResponse.getMethod());
		payment.setPaymentDate(tossResponse.getApprovedAt().toLocalDateTime()); // PAYMENT_DATE
		payment.setBillingStart(LocalDate.now());
		payment.setBillingEnd(LocalDate.now().plusMonths(contract.getSubsPeriod()));
		payment.setPaymentType("NORMAL");
		payment.setBillingKey(null);
		payment.setDiscountAmount(contract.getDiscountAmount());

		payment.setCreatedBy("SYSTEM");
		payment.setCreateDate(LocalDateTime.now());
		
		// ✅ 여기부터 “카드 결제인 경우에만” 실행
		if ("CARD".equalsIgnoreCase(method)) {
		    TossConfirmResponseVO.Card card = tossResponse.getCard();

		    if (card != null) { // 혹시 모르니 한 번 더 방어
		        String cardCode = card.getCardCompanyCode();
		        String maskedNumber = card.getNumber();  // 마스킹된 번호

		        String cardName = paymentMapper.findCardCompanyCode("OP", cardCode);

		        // 응답 객체에 세팅
		        tossResponse.setCardCompany(cardName);
		        tossResponse.setCardCompanyCode(cardCode);
		        tossResponse.setCardNumberMask(maskedNumber);

		        // 결제 VO에도 세팅
		        payment.setCardCompany(cardName);      // 또는 cardCode
		        payment.setCardNumberMask(maskedNumber);
		    }
		}
		// 고정데이터로 들어감
		// payment객체안에 값들을 채워넣음
//		paymentMapper.insertPayment(payment);
		// 셋팅된 payment객체를 mapper로 전달
		paymentMapper.insertPayment(payment);

		// ORDER 업데이트
		orderMapper.updateOrderSubCode(payment.getOrderId(), payment.getSubCode(), "SYSTEM");
//		orderMapper.updateOrderCompanyCode(payment.getOrderId(), company.getCompanyCode(), "SYSTEM");
		orderMapper.updateOrderBilling(order.getOrderId(), contract.getContractStart(), contract.getContractEnd());
		// 4. ORDER 테이블의 주문 상태 업데이트 (예: PAID / SUCCESS)

		orderMapper.updateOrderStatus(order.getOrderId(), "SUCCESS");
		
		// 회사 관리자 계정 생성
		Map<String, String> accountInfo = createCompanyManagerAccount(company);

		tossResponse.setUserId(accountInfo.get("userId"));
		tossResponse.setPassword(accountInfo.get("Password"));
		// 5. 그대로 Toss 응답 반환 (프론트가 필요로 하는 경우)
		return tossResponse;

	}

	@Override
	public List<SubscribeVO> selectInactiveSubListByComCode(String companyCode) {
		return paymentMapper.selectInactiveSubListByComCode(companyCode);
	}

	@Override
	public SubscribeVO selectSubDetail(String companyCode) {

		// 1) DB에서 구독 상세 조회
		SubscribeVO subDetail = paymentMapper.selectSubDetail(companyCode);

		LocalDate today = LocalDate.now();
		LocalDate endDate = subDetail.getSubsEnd();
		long diff = ChronoUnit.DAYS.between(today, endDate);
		subDetail.setRemainDays((int) diff);

		String label;
		if (diff > 0)
			label = "구독중";
		else if (diff == 0)
			label = "오늘 만료";
		else
			label = "만료됨";

		subDetail.setSubsStatusLabel(label);

		// 4) 최종 가공된 VO 반환
		return subDetail;
	}

	@Override
	public List<PaymentVO> selectPaymentHistory(String companyCode) {
		return paymentMapper.selectPaymentHistory(companyCode);
	}

	@Override
	/* @Transactional */
	public TossConfirmResponseVO createSubscriptionWithBillingKey(String authKey, String customerKey, CompanyVO company,
			PlanVO plan, ContractVO contract) {
		// ---------------- 1) 빌링키 발급 ----------------
		String billingKey = tossPaymentClient.issueBillingKey(authKey, customerKey);
		// 🔐 DB에 저장할 암호화된 빌링키
		String encryptedBillingKey = billingKeyCrypto.encrypt(billingKey);

		// ---------------- 2) 주문 생성 ----------------
		OrderVO order = new OrderVO();
		order.setOrderName(plan.getPlanName());
		order.setOrderAmount(contract.getTotalPrice().longValue());
		order.setOrderStatus("READY"); // 주문 상태
		order.setOrderType("BILLING"); // 필요시 상수/enum 처리
		order.setCreateDate(LocalDateTime.now());
		order.setUpdateDate(LocalDateTime.now());
		order.setCreatedBy("SYSTEM");
		order.setUpdatedBy("SYSTEM");
		order.setCompanyCode("0000");
		order.setPlanCode(plan.getPlanCode());
		System.out.println(order);

		orderMapper.insertOrder(order);

		// ---------------- 6) 빌링키로 첫 결제 승인 (정기결제) ----------------
		TossBillingConfirmRequestVO billingReq = new TossBillingConfirmRequestVO();
		billingReq.setBillingKey(billingKey);
		billingReq.setAmount(order.getOrderAmount()); // 주문 금액 기준
		billingReq.setOrderId(order.getOrderId());
		billingReq.setOrderName(plan.getPlanName()); // 예: "스탠다드 구독"
		billingReq.setCustomerKey(customerKey);

		TossConfirmResponseVO tossResponse = tossPaymentClient.confirmBillingPayment(billingReq);

		// ★ 결제 실패 시 롤백을 위해 상태 체크
		if (!"DONE".equalsIgnoreCase(tossResponse.getStatus())
				&& !"SUCCESS".equalsIgnoreCase(tossResponse.getStatus())) {
			throw new IllegalStateException("Billing payment failed. status=" + tossResponse.getStatus());
		}

		// 회사등록
		CompanyVO existCompany = companyMapper.selectCompany(company.getCompanyCode());
		if (existCompany == null) {
			company.setCreatedBy("SYSTEM");
			company.setCreateDate(LocalDateTime.now());
			company.setUpdatedBy("SYSTEM");
			company.setUpdateDate(LocalDateTime.now());
			company.setCompanyCode("0000");
			companyMapper.insertCompany(company); // 세션정보를 불러와 insert 매퍼실행
		} else {
			company = existCompany; // 기존 회사 정보 사용
		}

		// 계약서 등록

		contract.setCompanyCode(company.getCompanyCode());
		contract.setPlanCode(plan.getPlanCode());
//		contract.setCompanyName(company.getCompanyName());
		contract.setCompanyName("0000");
		contract.setCeoName(company.getCeoName());
		contract.setCreatedBy("SYSTEM");
		contract.setCreateDate(LocalDateTime.now());
		contract.setUpdatedBy("SYSTEM");
		contract.setUpdateDate(LocalDateTime.now());
		contract.setContractStart(LocalDateTime.now());
		contract.setContractEnd(contract.getContractStart().plusMonths(contract.getSubsPeriod()));

		contractMapper.insertContract(contract);

		// 구독생성
		LocalDate today = LocalDate.now();
		LocalDateTime now = LocalDateTime.now();
		int period = contract.getSubsPeriod();
		SubscribeVO subscribe = new SubscribeVO();
//		subscribe.setCompanyCode(company.getCompanyCode());
		subscribe.setCompanyCode("0000");
		subscribe.setSubsStatus("ACTIVE");
		subscribe.setSubsStart(LocalDate.now());
		subscribe.setSubsEnd(LocalDate.now().plusMonths(contract.getSubsPeriod()));
		subscribe.setCreatedBy("SYSTEM");
		subscribe.setCreateDate(LocalDateTime.now());
		subscribe.setUpdatedBy("SYSTEM");
		subscribe.setUpdateDate(LocalDateTime.now());
		subscribe.setPlanCode(plan.getPlanCode());
		subscribe.setContractCode(contract.getContractCode());
		// subscribe.setBillingPeriod();
		subscribe.setCurrentUserCount(contract.getUserCount());
		subscribe.setCurrentPrice(contract.getTotalPrice().doubleValue());
		subscribe.setRecentBillingDate(today);
		subscribe.setNextBillingDate(today.plusMonths(period));
		// ✅ 구독 테이블에는 "암호문" 저장
		subscribe.setBillingKey(encryptedBillingKey);

		subscribeMapper.insertSubscribe(subscribe);

		// 카드사 코드/이름 변환 (선택)
		String cardCode = tossResponse.getCard().getCardCompanyCode();
		String cardName = paymentMapper.findCardCompanyCode("OP", cardCode);
		String maskedNumber = tossResponse.getCard().getNumber(); // 마스킹 번호
		tossResponse.setCardCompany(cardName);
		tossResponse.setCardCompanyCode(cardCode);
		tossResponse.setCardNumberMask(maskedNumber);

		PaymentVO payment = new PaymentVO();

		payment.setOrderId(order.getOrderId());
		payment.setSubCode(subscribe.getSubCode());
//		payment.setCompanyCode(company.getCompanyCode());
		payment.setCompanyCode("0000");
		payment.setTotalPrice(tossResponse.getTotalAmount()); // TOTAL_PRICE
		payment.setPaymentStat(tossResponse.getStatus()); // PAYMENT_STAT (SUCCESS 등)
		payment.setPaymentKey(tossResponse.getPaymentKey()); // PAYMENT_KEY
		payment.setPaymentMethod(tossResponse.getMethod());
		payment.setCardCompany(cardCode);
		payment.setPaymentDate(tossResponse.getApprovedAt().toLocalDateTime()); // PAYMENT_DATE
		payment.setBillingStart(LocalDate.now());
		payment.setBillingEnd(LocalDate.now().plusMonths(1));
		payment.setPaymentType("BILLING");
		payment.setBillingKey(encryptedBillingKey);
		payment.setCardNumberMask(maskedNumber);

		payment.setCreatedBy("SYSTEM");
		payment.setCreateDate(LocalDateTime.now());
		payment.setUpdatedBy("SYSTEM");
		payment.setUpdateDate(LocalDateTime.now());

		paymentMapper.insertPayment(payment);

		// ORDER 업데이트
		orderMapper.updateOrderSubCode(payment.getOrderId(), payment.getSubCode(), "SYSTEM");
//		orderMapper.updateOrderCompanyCode(payment.getOrderId(), company.getCompanyCode(), "SYSTEM");
		orderMapper.updateOrderBilling(order.getOrderId(), contract.getContractStart(), contract.getContractEnd());
		// 4. ORDER 테이블의 주문 상태 업데이트 (예: PAID / SUCCESS)

		orderMapper.updateOrderStatus(order.getOrderId(), "SUCCESS");
		tossResponse.setBillingKey(null);

		int adminCount = userMapper.countCompanyManager(company.getCompanyCode());

		if (adminCount == 0) {
		    Map<String, String> accountInfo = 
		        createCompanyManagerAccount(company);

		    tossResponse.setUserId(accountInfo.get("userId"));
		    tossResponse.setPassword(accountInfo.get("password"));
		}
		
		return tossResponse;
	}

	@Override
	public TossConfirmResponseVO chargeSubscription(TossBillingConfirmRequestVO req) {

		// 3) 관련 구독 조회 (req에 subCode를 세팅해놨다고 가정)
		SubscribeVO sub = subscribeMapper.selectSubscribeBySubCode(req.getSubCode());
		if (sub == null) {
			throw new IllegalArgumentException("존재하지 않는 구독입니다. subCode=" + req.getSubCode());
		}
		   // 2) billingKey 복호화해서 세팅 (스케줄러/재시도에서 billingKey 주입 금지)
	    String encryptedBillingKey = sub.getBillingKey();
	    if (encryptedBillingKey == null || encryptedBillingKey.isBlank()) {
	        throw new IllegalStateException("결제수단(billingKey)이 없습니다. subCode=" + req.getSubCode());
	    }

		// 🔓 토스에 보낼 평문 빌링키
		String plainBillingKey = billingKeyCrypto.decrypt(encryptedBillingKey);

		// 2) 토스에 정기결제 승인 요청 (평문 사용)
		req.setBillingKey(plainBillingKey);

		// 1) 토스에 정기결제 승인 요청
		TossConfirmResponseVO tossResponse = tossPaymentClient.confirmBillingPayment(req);
		

	    // 4) 성공/실패 판단
	    String status = tossResponse.getStatus();
	    boolean success = "DONE".equalsIgnoreCase(status) || "SUCCESS".equalsIgnoreCase(status);

		// 2) (선택) 카드사 코드 → 카드사명 맵핑
		String cardCode = tossResponse.getCard().getCardCompanyCode();
		String cardName = paymentMapper.findCardCompanyCode("OP", cardCode);
		tossResponse.setCardCompany(cardName);
		tossResponse.setCardCompanyCode(cardCode);

		// 4) 결제 이력 INSERT
		PaymentVO payment = new PaymentVO();
		payment.setOrderId(req.getOrderId()); // 스케줄러에서 생성한 주문ID
		payment.setSubCode(sub.getSubCode());
		payment.setCompanyCode(sub.getCompanyCode());
		payment.setTotalPrice(tossResponse.getTotalAmount());
		payment.setPaymentStat(status);
		payment.setPaymentKey(tossResponse.getPaymentKey());
		payment.setPaymentMethod(tossResponse.getMethod());
		payment.setCardCompany(cardCode);
		payment.setPaymentDate(tossResponse.getApprovedAt().toLocalDateTime());
		payment.setBillingStart(LocalDate.now());
		payment.setBillingEnd(LocalDate.now().plusMonths(1)); // 매달 결제 가정

		payment.setPaymentType("BILLING"); // 정기결제 구분값
		// ✅ 어떤 빌링키로 결제했는지 "암호문"으로 저장
		payment.setBillingKey(encryptedBillingKey);

		payment.setCreatedBy("SYSTEM");
		payment.setCreateDate(LocalDateTime.now());
		payment.setUpdatedBy("SYSTEM");
		payment.setUpdateDate(LocalDateTime.now());

		paymentMapper.insertPayment(payment);

		   // 7) 주문 상태 업데이트 (기존 mapper 재사용)
	    orderMapper.updateOrderStatus(req.getOrderId(), success ? "SUCCESS" : "FAILED");
		
	    // 8) 구독 상태/청구일 갱신 (⭐ 성공일 때만 nextBillingDate 갱신!)
	    if (success) {
	        LocalDate today = LocalDate.now();
	        LocalDate next = today.plusMonths(1);
	        subscribeMapper.updateBillingDates(sub.getSubCode(), today, next);
	        subscribeMapper.updateSubsStatus(sub.getSubCode(), "ACTIVE", "SYSTEM");
	    } else {
	        subscribeMapper.updateSubsStatus(sub.getSubCode(), "PAST_DUE", "SYSTEM");
	    }
	    
		return tossResponse; // 스케줄러 내부에서 결과 안 쓰면 반환 없어도 되긴 함
		
	}
	
	//결제수단 변경
	@Transactional
	public void changeBillingMethod(String subCode, String authKey, String customerKey) {

	    SubscribeVO sub = subscribeMapper.selectSubscribeBySubCode(subCode);
	    if (sub == null) {
	        throw new IllegalArgumentException("존재하지 않는 구독입니다. subCode=" + subCode);
	    }

	    // 1) 토스에서 새 billingKey 발급
	    String newBillingKeyPlain = tossPaymentClient.issueBillingKey(authKey, customerKey);
	    String newBillingKeyEnc = billingKeyCrypto.encrypt(newBillingKeyPlain);

	    // 2) 구독 테이블 billingKey 교체
	    // ※ 아래 updateBillingKey 쿼리/매퍼 추가 필요
	    subscribeMapper.updateBillingKey(subCode, newBillingKeyEnc, "SYSTEM");
	}
	
	//결제 재시도
	@Transactional
	public TossConfirmResponseVO retryBillingPayment(String subCode) {

	    SubscribeVO sub = subscribeMapper.selectSubscribeBySubCode(subCode);
	    if (sub == null) {
	        throw new IllegalArgumentException("존재하지 않는 구독입니다. subCode=" + subCode);
	    }
	    if (sub.getBillingKey() == null || sub.getBillingKey().isBlank()) {
	        throw new IllegalStateException("결제수단이 등록되어 있지 않습니다. 결제수단 변경부터 하세요.");
	    }

	    // ✅ 주문금액 결정 (반올림)
	    long orderAmount = Math.round(sub.getCurrentPrice());
	    if (orderAmount <= 0) {
	        throw new IllegalStateException("주문금액이 올바르지 않습니다. currentPrice=" + sub.getCurrentPrice());
	    }

	    // 1) 주문 생성(재시도용)
	    OrderVO order = new OrderVO();
	    order.setOrderName("정기결제 재시도");
	    order.setOrderAmount(orderAmount);
	    order.setOrderStatus("READY");
	    order.setOrderType("BILLING");
	    order.setCreateDate(LocalDateTime.now());
	    order.setCreatedBy("SYSTEM");
	    order.setCompanyCode(sub.getCompanyCode());
	    orderMapper.insertOrder(order);

	    // 2) 결제 요청 만들어서 공통함수 호출
	    TossBillingConfirmRequestVO req = new TossBillingConfirmRequestVO();
	    req.setSubCode(subCode);
	    req.setOrderId(order.getOrderId());
	    req.setAmount(order.getOrderAmount());
	    req.setOrderName(order.getOrderName());

	    return chargeSubscription(req);
	    
	  
	}
	
	//만료 재구독
	@Transactional
	public TossConfirmResponseVO resubscribeBilling(String subCode, String customerKey) {

	    SubscribeVO sub = subscribeMapper.selectSubscribeBySubCode(subCode);
	    if (sub == null) {
	        throw new IllegalArgumentException("존재하지 않는 구독입니다. subCode=" + subCode);
	    }
	    if (sub.getBillingKey() == null || sub.getBillingKey().isBlank()) {
	        throw new IllegalStateException("결제수단이 등록되어 있지 않습니다. 결제수단 변경부터 하세요.");
	    }

	    LocalDate today = LocalDate.now();
	    if (sub.getSubsEnd() != null && !sub.getSubsEnd().isBefore(today)) {
	        // 정책: 만료가 아닌데 재구독 버튼 눌렀으면 막기(원하면 연장으로 바꿀 수도 있음)
	        throw new IllegalStateException("재구독 대상이 아닙니다. 아직 만료되지 않았습니다.");
	    }
	    // ✅ 주문금액 결정: 현재 구독 청구 금액 사용
	    long orderAmount = (long) Math.round(sub.getCurrentPrice()); // double -> long 안전하게 반올림

	    if (orderAmount <= 0) {
	        throw new IllegalStateException("주문금액이 올바르지 않습니다. currentPrice=" + sub.getCurrentPrice());
	    }

	    // 1) 주문 생성(재구독용)
	    OrderVO order = new OrderVO();
	    order.setOrderName("만료 재구독 결제");
	    order.setOrderAmount(orderAmount);
	    order.setOrderStatus("READY");
	    order.setOrderType("BILLING");
	    order.setCreateDate(LocalDateTime.now());
	    order.setCreatedBy("SYSTEM");
	    order.setCompanyCode(sub.getCompanyCode());
	    orderMapper.insertOrder(order);

	    // 2) 결제 실행 (공통함수 재사용)
	    TossBillingConfirmRequestVO req = new TossBillingConfirmRequestVO();
	    req.setSubCode(subCode);
	    req.setOrderId(order.getOrderId());
	    req.setAmount(order.getOrderAmount());
	    req.setOrderName(order.getOrderName());
	    req.setCustomerKey(customerKey);

	    TossConfirmResponseVO payRes = chargeSubscription(req);

	    // 3) ✅ 구독 상태/기간 갱신(재활성화)
	    int periodMonths = (sub.getBillingPeriod() != null ? sub.getBillingPeriod() : 1);
	    LocalDate newStart = today;
	    LocalDate newEnd = today.plusMonths(periodMonths);

	    // ※ 아래 updateResubscribe 쿼리/매퍼 추가 필요
	    subscribeMapper.updateResubscribe(
	        subCode,
	        "ACTIVE",
	        newStart,
	        newEnd,
	        today,
	        today.plusMonths(1),
	        "SYSTEM"
	    );

	    return payRes;
	}


	@Override
	public Map<String, String> createCompanyManagerAccount(CompanyVO company) {

		Map<String, String> result = new HashMap<>();
		Date now = new Date();

		/* 1) 임시 비밀번호 생성 */
		String rawPassword = PasswordUtil.generateRandomPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);

		// 4) LOGIN_MST INSERT
		SbLoginVO login = new SbLoginVO();
		login.setCompanyCode(company.getCompanyCode());
		login.setPassWord(encodedPassword); // 암호문만 DB 저장
		login.setUserName("-");
		login.setStatus("ACTIVE");
		login.setFailCount(0);
		login.setCreatedBy("SYSTEM");
		login.setCreateDate(now);
		login.setUpdatedBy("SYSTEM");
		login.setUpdateDate(now);

		userMapper.insertLogin(login);

		// 자동 생성된 USER_ID 가져오기
		String userId = login.getUserId();

		// 2) USER_MST INSERT
		SbUserVO user = new SbUserVO();
		user.setCompanyCode(company.getCompanyCode());
		user.setUserId(userId);
		user.setUserName("-");
		user.setSalary(0);
		user.setDept("-");
		user.setJobTitle("-");
		user.setPosition("-");
		user.setCreatedBy("SYSTEM");
		user.setCreateDate(now);
		user.setUpdatedBy("SYSTEM");
		user.setUpdateDate(now);
		user.setRoleCode("ADMIN");

		userMapper.insertUser(user);
		
		userMapper.insertRoleMenu(company.getCompanyCode());
		

		// 6) 화면에 보여줄 정보만 반환
		result.put("userId", userId);
		result.put("password", rawPassword);

		return result;
	}
}
