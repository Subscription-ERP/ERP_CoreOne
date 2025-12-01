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

	private BigDecimal calculateBaseTotal(PlanVO plan) {
	    BigDecimal price = plan.getPrice();
	    Integer subsPeriod = plan.getSubsPeriod();
	    return price.multiply(BigDecimal.valueOf(subsPeriod)); // 월요금 × 개월수
	}
	@Override
	public BigDecimal calculateVat(PlanVO plan) {
	    BigDecimal baseTotal = calculateBaseTotal(plan);
	    BigDecimal vat = baseTotal.multiply(VAT_RATE);
	    return vat.setScale(0, RoundingMode.HALF_UP);
	}
	@Override
	public BigDecimal calculateTotalPrice(PlanVO plan) {
	    BigDecimal baseTotal = calculateBaseTotal(plan);
	    BigDecimal vat = baseTotal.multiply(VAT_RATE);
	    BigDecimal finalPrice = baseTotal.add(vat);
	    return finalPrice.setScale(0, RoundingMode.HALF_UP);
	}
}