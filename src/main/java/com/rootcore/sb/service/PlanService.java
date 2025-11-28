package com.rootcore.sb.service;

import java.util.List;

import com.rootcore.sb.vo.PlanVO;

public interface PlanService {
	 // 플랜 목록 조회
    List<PlanVO> getPlanList();

    // 플랜 상세 조회 (계약서에 넘길 때 사용)
    PlanVO getPlanDetail(String planCode);
}
