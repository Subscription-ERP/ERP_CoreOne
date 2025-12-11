package com.rootcore.sb.service;

import java.math.BigDecimal;
import java.util.List;

import com.rootcore.sb.vo.PlanVO;

public interface PlanService {
	 // 플랜 목록 조회
    List<PlanVO> getPlanList();

    // 플랜 상세 조회 (계약서에 넘길 때 사용)
    PlanVO getPlanDetail(String planCode);
    
    BigDecimal calculateTotalPrice(PlanVO plan);
    BigDecimal calculateVat(PlanVO plan);
    BigDecimal calculateDiscountedBase(PlanVO plan);
    BigDecimal calculateBaseTotal(PlanVO plan);
    BigDecimal calculateDiscountAmount(PlanVO plan);
    BigDecimal getDiscountedBase(PlanVO plan);
}
