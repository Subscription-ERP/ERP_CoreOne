package com.rootcore.hr.service;

import java.util.List;

import com.rootcore.hr.vo.AnnualLeaveVO;

/*
 * 연차현황조회 서비스
 * AnnualStatusService.java
 */
public interface AnnualStatusService {
	
	// 연차현황조회 - 다건조회
	List<AnnualLeaveVO> selectAnnualStatusList(AnnualLeaveVO param);

}
