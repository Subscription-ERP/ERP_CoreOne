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
	private final PlanMapper planMapper;
	// 연간 선결제 할인율 (10%) / 부가세 (10%)
	private static final BigDecimal VAT_RATE = new BigDecimal("0.10");
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
	@Override
	public BigDecimal calculateBaseTotal(PlanVO plan) {
	    BigDecimal price = plan.getPrice();              // 월 요금 (예: 15000)
	    Integer subsPeriod = plan.getSubsPeriod();       // 계약기간 (개월, 예: 12)

	    if (price == null || subsPeriod == null) {
	        return BigDecimal.ZERO;
	    }

	    return price.multiply(BigDecimal.valueOf(subsPeriod)); // 월요금 × 개월수
	}

	// (2) 할인까지 반영된 실질 기본 금액
	@Override
	public BigDecimal getDiscountedBase(PlanVO plan) {
	    BigDecimal baseTotal = calculateBaseTotal(plan);

	    Integer billingPeriod = plan.getBillingPeriod(); // 1(월 결제) or 12(연간 선결제)
	    Integer subsPeriod = plan.getSubsPeriod();       // 계약기간(개월)
	    
	    // 기본값: 할인 없음
	    BigDecimal discountedBase = baseTotal;


	    // 연간 선결제 + 12개월 계약일 때만 10% 할인
	    if (billingPeriod != null && billingPeriod == 12
	            && subsPeriod != null && subsPeriod == 12) {

	        BigDecimal discount = baseTotal.multiply(YEARLY_DISCOUNT_RATE); // 10% 할인액
	        discountedBase = baseTotal.subtract(discount);
	    }
	    
	    // 혹시 음수 방지
	    if (discountedBase.compareTo(BigDecimal.ZERO) < 0) {
	        return BigDecimal.ZERO;
	    }

	    return discountedBase;
	}

	// (2) 할인까지 반영된 실질 기본 금액 (외부에서 쓰는 공식 메서드)
	@Override
	public BigDecimal calculateDiscountedBase(PlanVO plan) {
	    return getDiscountedBase(plan);
	}

	
	// 순수 할인 금액 (얼마 깎였는지)
	public BigDecimal calculateDiscountAmount(PlanVO plan) {
	    BigDecimal baseTotal      = calculateBaseTotal(plan);   // 할인 전
	    BigDecimal discountedBase = getDiscountedBase(plan);    // 할인 후

	    BigDecimal discount = baseTotal.subtract(discountedBase);	// 할인적용후 금액

	    if (discount.compareTo(BigDecimal.ZERO) < 0) {
	        return BigDecimal.ZERO;
	    }
	    return discount;
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
		BigDecimal discountedBase = getDiscountedBase(plan);    // 할인 후 금액
	    BigDecimal vat            = calculateVat(plan);         // 위에서 이미 반올림 처리
	    BigDecimal total          = discountedBase.add(vat);
	    return total.setScale(0, RoundingMode.HALF_UP);
	}
}