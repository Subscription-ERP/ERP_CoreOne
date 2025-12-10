package com.rootcore.sb.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.sb.mapper.PlanMapper;
import com.rootcore.sb.service.PlanService;
import com.rootcore.sb.vo.PlanVO;

@Service
public class PlanServiceImpl implements PlanService {
	private static final BigDecimal VAT_RATE = new BigDecimal("0.10");
	private final PlanMapper planMapper;
	// 연간 선결제 할인율 (10%)
	private static final BigDecimal YEARLY_DISCOUNT_RATE = new BigDecimal("0.10");


	public PlanServiceImpl(PlanMapper planMapper) {
		this.planMapper = planMapper;
	}

	@Override
	public List<PlanVO> getPlanList() {
		return planMapper.selectPlanList();

	}

	@Override
	public PlanVO getPlanDetail(String planCode) {
		PlanVO p = planMapper.selectPlanByCode(planCode);

		return p;
	}

	// (1) 계약기간 × 월요금 (할인 전 금액)
	private BigDecimal calculateBaseTotal(PlanVO plan) {
	    BigDecimal price = plan.getPrice();              // 월 요금 (예: 15000)
	    Integer subsPeriod = plan.getSubsPeriod();       // 계약기간 (개월, 예: 12)

	    if (price == null || subsPeriod == null) {
	        return BigDecimal.ZERO;
	    }

	    return price.multiply(BigDecimal.valueOf(subsPeriod)); // 월요금 × 개월수
	}

	// (2) 할인까지 반영된 실질 기본 금액
	private BigDecimal calculateDiscountedBase(PlanVO plan) {
	    BigDecimal baseTotal = calculateBaseTotal(plan);

	    Integer billingPeriod = plan.getBillingPeriod(); // 1(월 결제) or 12(연간 선결제)
	    Integer subsPeriod = plan.getSubsPeriod();       // 계약기간(개월)

	    // 연간 선결제 + 12개월 계약일 때만 10% 할인
	    if (billingPeriod != null && billingPeriod == 12
	            && subsPeriod != null && subsPeriod == 12) {

	        BigDecimal discount = baseTotal.multiply(YEARLY_DISCOUNT_RATE); // 10% 할인액
	        baseTotal = baseTotal.subtract(discount);
	    }

	    return baseTotal;
	}

	// (3) VAT 계산 (할인 반영된 금액 기준)
	@Override
	public BigDecimal calculateVat(PlanVO plan) {
	    BigDecimal discountedBase = calculateDiscountedBase(plan); // ✅ 여기 중요
	    BigDecimal vat = discountedBase.multiply(VAT_RATE);
	    return vat.setScale(0, RoundingMode.HALF_UP);
	}

	// (4) 최종 결제 금액 (할인 + VAT 포함)
	@Override
	public BigDecimal calculateTotalPrice(PlanVO plan) {
	    BigDecimal discountedBase = calculateDiscountedBase(plan); // ✅
	    BigDecimal vat = discountedBase.multiply(VAT_RATE);
	    BigDecimal finalPrice = discountedBase.add(vat);
	    return finalPrice.setScale(0, RoundingMode.HALF_UP);
	}
}