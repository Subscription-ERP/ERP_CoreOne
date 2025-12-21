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
import com.rootcore.sb.service.MailService;
import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.service.TossCardInfoEnricher;
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

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
	private final TossCardInfoEnricher tossCardInfoEnricher;
	
	private final MailService mailService;

	@Value("${project.url}")
	String url;

	/**
	 * 회사 등록이 끝난 상태에서: 회사코드 + 플랜정보 + 금액을 가지고 주문을 생성하고 프론트에서 Toss 위젯을 띄울 수 있도록 값 반환
	 */
	@Override
	public PaymentReadyResponseVO insertOrder(OrderVO ordervo, PlanVO plan) {

		// ✅ 주문 정보 ORDER 테이블에 저장
		ordervo.setOrderStatus("READY"); // 주문 상태
		ordervo.setOrderType("NORMAL"); // 필요시 상수/enum 처리
		ordervo.setCreateDate(LocalDateTime.now());
		ordervo.setPlanCode(plan.getPlanCode());
		ordervo.setCreatedBy("SYSTEM"); // 나중에 로그인 사용자로 교체
		
		  // ✅ companyCode가 없으면(최초가입) 임시값 0000 세팅
	    if (ordervo.getCompanyCode() == null || ordervo.getCompanyCode().isBlank()) {
	        ordervo.setCompanyCode("0000");
	    }
		
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
			ContractVO contract, HttpSession session) {

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

		// ✅ 3) 재구독이면 주문에 companyCode가 들어있음
	    String orderCompanyCode = order.getCompanyCode();
	    boolean isFirstJoin = (orderCompanyCode == null || "0000".equals(orderCompanyCode));

	    // 4) 회사 확정
	    CompanyVO realcompany;
	    
	    // ✅ 회사 확정
	    CompanyVO existCompany = null;
	    if (orderCompanyCode != null) {
	        existCompany = companyMapper.selectCompany(orderCompanyCode);
	        if (existCompany == null) {
	            throw new IllegalStateException("주문에 저장된 회사코드의 회사가 존재하지 않습니다.");
	        }
	        company = existCompany; // 재구독은 기존회사 사용
	    } else {
	        // ✅ 최초결제(비로그인): 세션 company로 회사 등록
	        if (company == null) throw new IllegalStateException("회사 등록 정보가 없습니다.");
	        existCompany = companyMapper.selectCompany(company.getCompanyCode());
	        if (existCompany == null) {
	            company.setCreatedBy("SYSTEM");
	            company.setCreateDate(LocalDateTime.now());
	            companyMapper.insertCompany(company);
	        } else {
	            company = existCompany;
	        }
	        // 주문에도 회사코드 업데이트 해두면 좋음(선택)
	         orderMapper.updateOrderCompanyCode(order.getOrderId(), company.getCompanyCode(), "SYSTEM");
	    }
		// 계약서 등록

		contract.setCompanyCode(company.getCompanyCode());
		contract.setPlanCode(plan.getPlanCode());
		contract.setCompanyName(company.getCompanyName());
		contract.setCeoName(company.getCeoName());
		contract.setCreateDate(LocalDateTime.now());
		contract.setCreatedBy("SYSTEM");
		contract.setContractStart(LocalDateTime.now());
		contract.setContractEnd(contract.getContractStart().plusMonths(contract.getSubsPeriod()));
		contractMapper.insertContract(contract);

		 subscribeMapper.expireActiveSubscribe(company.getCompanyCode(), LocalDate.now(), "SYSTEM", LocalDateTime.now());
		
		// 구독생성
		LocalDate today = LocalDate.now();
		SubscribeVO subscribe = new SubscribeVO();
		subscribe.setCompanyCode(company.getCompanyCode());
		subscribe.setSubsStatus("ACTIVE");
		subscribe.setSubsStart(LocalDate.now());
		subscribe.setSubsEnd(LocalDate.now().plusMonths(contract.getSubsPeriod()));
		subscribe.setCreatedBy("SYSTEM");
		subscribe.setCreateDate(LocalDateTime.now());
		subscribe.setPlanCode(plan.getPlanCode());
		subscribe.setContractCode(contract.getContractCode());
		subscribe.setBillingPeriod(contract.getBillingPeriod());
		subscribe.setCurrentUserCount(contract.getUserCount());
		subscribe.setCurrentPrice(contract.getTotalPrice().doubleValue());
		subscribe.setRecentBillingDate(today);
		subscribe.setNextBillingDate(null);
		subscribe.setBillingPeriod(contract.getBillingPeriod());

		subscribeMapper.insertSubscribe(subscribe);

		// 3. PAYMENT 테이블에 결제 이력 INSERT
		// payment 객체생성
		PaymentVO payment = new PaymentVO();

		payment.setOrderId(order.getOrderId());
		payment.setSubCode(subscribe.getSubCode());
		payment.setCompanyCode(company.getCompanyCode());
		payment.setTotalPrice(tossResponse.getTotalAmount()); // TOTAL_PRICE
		payment.setPaymentStat(tossResponse.getStatus()); // PAYMENT_STAT (SUCCESS 등)
		payment.setPaymentKey(tossResponse.getPaymentKey()); // PAYMENT_KEY
		payment.setPaymentMethod(tossResponse.getMethod());
		payment.setPaymentDate(tossResponse.getApprovedAt().toLocalDate()); // PAYMENT_DATE
		payment.setBillingStart(LocalDate.now());
		payment.setBillingEnd(LocalDate.now().plusMonths(contract.getSubsPeriod()));
		payment.setPaymentType("NORMAL");
		payment.setBillingKey(null);
		payment.setDiscountAmount(contract.getDiscountAmount());

		payment.setCreatedBy("SYSTEM");
		payment.setCreateDate(LocalDateTime.now());

		tossCardInfoEnricher.applyCardInfo(tossResponse, payment);
		// 고정데이터로 들어감
		// payment객체안에 값들을 채워넣음
//		paymentMapper.insertPayment(payment);
		// 셋팅된 payment객체를 mapper로 전달
		paymentMapper.insertPayment(payment);

		// ORDER 업데이트
		orderMapper.updateOrderSubCode(payment.getOrderId(), payment.getSubCode(), "SYSTEM");
		orderMapper.updateOrderBilling(order.getOrderId(), contract.getContractStart(), contract.getContractEnd());
		// 4. ORDER 테이블의 주문 상태 업데이트 (예: PAID / SUCCESS)

		orderMapper.updateOrderStatus(order.getOrderId(), "SUCCESS");

		// 회사 관리자 계정 생성
		// ✅ 8) 관리자 계정 생성은 “최초 회사 등록”일 때만 하는 게 안전
	    // 재구독이면 기존 계정이 있을 테니 생성하면 중복됨
	    if (orderCompanyCode == null) {
	        Map<String, String> accountInfo = createCompanyManagerAccount(company);
	        tossResponse.setUserId(accountInfo.get("userId"));
	        tossResponse.setPassword(accountInfo.get("password"));
	    }
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

		String subsLabel;
		if (diff > 0)
			subsLabel = "구독중";
		else if (diff == 0)
			subsLabel = "오늘 만료";
		else
			subsLabel = "만료됨";
		
		String status = subDetail.getPaymentStat();
		String paymentLabel;
		if ("DONE".equals(status))
			paymentLabel  ="결제완료";
		else 
			paymentLabel  = "결제실패";
	
		subDetail.setPaymentStatLabel(paymentLabel);
		subDetail.setSubsStatusLabel(subsLabel);

		// 4) 최종 가공된 VO 반환
		return subDetail;
	}

	@Override
	public List<PaymentVO> selectPaymentHistory(String companyCode) {
		return paymentMapper.selectPaymentHistory(companyCode);
	}

	@Override
	@Transactional
	public TossConfirmResponseVO createSubscriptionWithBillingKey(String authKey, String customerKey, CompanyVO company,
			PlanVO plan, ContractVO contract) {
		// ---------------- 1) 빌링키 발급 ----------------
		String billingKey = tossPaymentClient.issueBillingKey(authKey, customerKey);
		// 🔐 DB에 저장할 암호화된 빌링키
		String encryptedBillingKey = billingKeyCrypto.encrypt(billingKey);

		// ---------------- 2) 주문 생성 ----------------
		OrderVO billingorder = new OrderVO();
		billingorder.setOrderName(plan.getPlanName());
		billingorder.setOrderAmount(contract.getTotalPrice().longValue());
		billingorder.setOrderStatus("READY"); // 주문 상태
		billingorder.setOrderType("BILLING"); // 필요시 상수/enum 처리
		billingorder.setCreateDate(LocalDateTime.now());
		billingorder.setUpdateDate(LocalDateTime.now());
		billingorder.setCreatedBy("SYSTEM");
		billingorder.setUpdatedBy("SYSTEM");
		billingorder.setCompanyCode("0000");
		billingorder.setPlanCode(plan.getPlanCode());

		orderMapper.insertOrder(billingorder);

		
		
		// ---------------- 6) 빌링키로 첫 결제 승인 (정기결제) ----------------
		TossBillingConfirmRequestVO billingReq = new TossBillingConfirmRequestVO();
		billingReq.setBillingKey(billingKey);
		billingReq.setAmount(billingorder.getOrderAmount()); // 주문 금액 기준
		billingReq.setOrderId(billingorder.getOrderId());
		billingReq.setOrderName(plan.getPlanName()); // 예: "스탠다드 구독"
		billingReq.setCustomerKey(customerKey);

		log.info("[BILLING-FIRST] orderId={}, customerKey={}, amount={}",
		        billingorder.getOrderId(), customerKey, billingorder.getOrderAmount());
		
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
			company.setCompanyCode(company.getCompanyCode());
			company.setCustomerKey(customerKey);
			
			companyMapper.insertCompany(company); // 세션정보를 불러와 insert 매퍼실행
		} else {
			company = existCompany; // 기존 회사 정보 사용
		}

		// 계약서 등록

		contract.setCompanyCode(company.getCompanyCode());
		contract.setPlanCode(plan.getPlanCode());
		contract.setCompanyName(company.getCompanyName());
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
		LocalDate next = today.plusMonths(1);
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
		// subscribe.setBillingPeriod();
		subscribe.setCurrentUserCount(contract.getUserCount());
		subscribe.setCurrentPrice(contract.getTotalPrice().doubleValue());
		subscribe.setRecentBillingDate(today);
		subscribe.setNextBillingDate(next);
		subscribe.setBillingPeriod(contract.getBillingPeriod());
		// ✅ 구독 테이블에는 "암호문" 저장
		subscribe.setBillingKey(encryptedBillingKey);

		subscribeMapper.insertSubscribe(subscribe);

		PaymentVO payment = new PaymentVO();
		payment.setOrderId(billingorder.getOrderId());
		payment.setSubCode(subscribe.getSubCode());
		payment.setCompanyCode(company.getCompanyCode());
		payment.setTotalPrice(tossResponse.getTotalAmount()); // TOTAL_PRICE
		payment.setPaymentStat(tossResponse.getStatus()); // PAYMENT_STAT (SUCCESS 등)
		payment.setPaymentKey(tossResponse.getPaymentKey()); // PAYMENT_KEY
		payment.setPaymentMethod(tossResponse.getMethod());
		payment.setPaymentDate(tossResponse.getApprovedAt().toLocalDate()); // PAYMENT_DATE
		payment.setBillingStart(LocalDate.now());
		payment.setBillingEnd(LocalDate.now().plusMonths(1));
		payment.setPaymentType("BILLING");
		payment.setBillingKey(encryptedBillingKey);

		payment.setCreatedBy("SYSTEM");
		payment.setCreateDate(LocalDateTime.now());
		payment.setUpdatedBy("SYSTEM");
		payment.setUpdateDate(LocalDateTime.now());

		tossCardInfoEnricher.applyCardInfo(tossResponse, payment);

		paymentMapper.insertPayment(payment);

		// ORDER 업데이트
		orderMapper.updateOrderSubCode(payment.getOrderId(), payment.getSubCode(), "SYSTEM");
		orderMapper.updateOrderCompanyCode(payment.getOrderId(), company.getCompanyCode(), "SYSTEM");
		orderMapper.updateOrderBilling(billingorder.getOrderId(), contract.getContractStart(), contract.getContractEnd());
		// 4. ORDER 테이블의 주문 상태 업데이트 (예: PAID / SUCCESS)

		orderMapper.updateOrderStatus(billingorder.getOrderId(), "SUCCESS");
		tossResponse.setBillingKey(null);

		int adminCount = userMapper.countCompanyManager(company.getCompanyCode());

		if (adminCount == 0) {
			Map<String, String> accountInfo = createCompanyManagerAccount(company);

			tossResponse.setUserId(accountInfo.get("userId"));
			tossResponse.setPassword(accountInfo.get("password"));
			tossResponse.setCompanyCode(company.getCompanyCode());
		}

		return tossResponse;
	}

	@Transactional
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
		boolean success = "DONE".equalsIgnoreCase(status);

		// 4) 결제 이력 INSERT
		PaymentVO payment = new PaymentVO();
		payment.setOrderId(req.getOrderId()); // 스케줄러에서 생성한 주문ID
		payment.setSubCode(sub.getSubCode());
		payment.setCompanyCode(sub.getCompanyCode());
		payment.setTotalPrice(tossResponse.getTotalAmount());
		payment.setPaymentStat(status);
		payment.setPaymentKey(tossResponse.getPaymentKey());
		payment.setPaymentMethod(tossResponse.getMethod());
		if (tossResponse.getApprovedAt() != null) {
			payment.setPaymentDate(tossResponse.getApprovedAt().toLocalDate());
		} else {
			payment.setPaymentDate(LocalDate.now()); // 또는 null 허용
		}
		if (success) {
			payment.setBillingStart(LocalDate.now());
			payment.setBillingEnd(LocalDate.now().plusMonths(1));
		}
		payment.setPaymentType("BILLING"); // 정기결제 구분값
		// ✅ 어떤 빌링키로 결제했는지 "암호문"으로 저장
		payment.setBillingKey(encryptedBillingKey);

		payment.setCreatedBy("SYSTEM");
		payment.setCreateDate(LocalDateTime.now());
		payment.setUpdatedBy("SYSTEM");
		payment.setUpdateDate(LocalDateTime.now());

		tossCardInfoEnricher.applyCardInfo(tossResponse, payment);

		paymentMapper.insertPayment(payment);

		// 7) 주문 상태 업데이트 (기존 mapper 재사용)
		orderMapper.updateOrderStatus(req.getOrderId(), success ? "SUCCESS" : "FAILED");

		// 8) 구독 상태/청구일 갱신 (⭐ 성공일 때만 nextBillingDate 갱신!)
		if (success) {
			LocalDate today = LocalDate.now();
			LocalDate next = today.plusMonths(1);
			subscribeMapper.updateBillingDates(today, next, sub.getSubCode());
			subscribeMapper.updateSubsStatus(sub.getSubCode(), "ACTIVE", "SYSTEM");
		} else {
			subscribeMapper.updateSubsStatus(sub.getSubCode(), "PAST_DUE", "SYSTEM");
		}

		return tossResponse; // 스케줄러 내부에서 결과 안 쓰면 반환 없어도 되긴 함

	}
	
	@Transactional
	@Override
	public Map<String, String> createCompanyManagerAccount(CompanyVO company) {

		Map<String, String> result = new HashMap<>();
		Date now = new Date();
		
		   // 0) 담당자 이메일 필수 체크
        if (company.getManagerEmail() == null || company.getManagerEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("담당자 이메일이 없어 계정 안내 메일을 발송할 수 없습니다.");
        }


		/* 1) 임시 비밀번호 생성 */
		String rawPassword = PasswordUtil.generateRandomPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);

		// 4) LOGIN_MST INSERT
		SbLoginVO login = new SbLoginVO();
		login.setCompanyCode(company.getCompanyCode());
		login.setPassword(encodedPassword); // 암호문만 DB 저장
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
		
		 // 5) ✅ 이메일 발송 (임시 비밀번호는 메일로만)
        mailService.sendAccountMail(company.getManagerEmail(), userId, rawPassword,company.getCompanyCode());

		// 6) 화면에 보여줄 정보만 반환
		result.put("userId", userId);
		result.put("password", rawPassword);

		return result;
	}

	
}
