package com.rootcore.sb.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rootcore.sb.mapper.CompanyMapper;
import com.rootcore.sb.mapper.OrderMapper;
import com.rootcore.sb.mapper.PlanMapper;
import com.rootcore.sb.mapper.SubscribeMapper;
import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.vo.CompanyVO;
import com.rootcore.sb.vo.OrderVO;
import com.rootcore.sb.vo.PlanVO;
import com.rootcore.sb.vo.SubscribeVO;
import com.rootcore.sb.vo.TossBillingConfirmRequestVO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BillingScheduler {

	private final OrderMapper orderMapper; // 주문
    private final SubscribeMapper subscribeMapper;
    private final PlanMapper planMapper;
    private final CompanyMapper companyMapper;
    private final PaymentService paymentService;

    /**
     * 매일 새벽 3시에 정기결제 수행
     * cron = 초 분 시 일 월 요일
     * 0 0 3 * * *  → 매일 03:00:00
     */
    @Scheduled(cron = "0 02 13 * * *")
    public void runMonthlyBillingScheduler() {

        LocalDate today = LocalDate.now();

        // 1) 오늘 결제해야 하는 구독 목록 조회
        List<SubscribeVO> list = subscribeMapper.selectNeedBilling(today);

        if (list.isEmpty()) {
            return;
        }

        for (SubscribeVO sub : list) {
        	
        	 // 2) 구독에 연결된 회사, 플랜 정보 조회
            CompanyVO company = companyMapper.selectCompany(sub.getCompanyCode());
            PlanVO plan = planMapper.selectPlanByCode(sub.getPlanCode());
            try {
            	long amount = Math.round(sub.getCurrentPrice());
            	OrderVO order = new OrderVO();
        		order.setOrderName("정기결제 자동청구");
        		order.setOrderAmount(amount);
        		order.setOrderStatus("READY"); // 주문 상태
        		order.setOrderType("BILLING"); // 필요시 상수/enum 처리
        		order.setCreateDate(LocalDateTime.now());
        		order.setUpdateDate(LocalDateTime.now());
        		order.setCreatedBy("SYSTEM"); // 나중에 로그인 사용자로 교체
        		order.setUpdatedBy("SYSTEM");
        		order.setCompanyCode(company.getCompanyCode());
        		order.setPlanCode(plan.getPlanCode());

        		orderMapper.insertOrder(order);
            	
        		
                // 2) 정기결제 요청 데이터 만들기
                TossBillingConfirmRequestVO req = new TossBillingConfirmRequestVO();
                req.setAmount(amount);
                req.setOrderName(order.getOrderName());

                // 주문번호 규칙 예시: BILL_{subCode}_{timestamp}
                req.setOrderId(order.getOrderId());
                // 필요하면 subCode도 넣어두기
                req.setSubCode(sub.getSubCode());

                // 3) 한 건의 결제 처리 (핵심 로직은 서비스에서)
                paymentService.chargeSubscription(req);

            } catch (Exception e) {
                // 여기서 예외 잡고 로깅
                // 실패한 건만 따로 알림 보내거나, 재시도 플래그 남길 수도 있음
                e.printStackTrace();
            }
        }
    }
}
