package com.rootcore.hr.mapper;

import java.util.List;

import com.rootcore.hr.vo.AnnualLeaveVO;

/*
 * 연차현황조회 매퍼
 * AnnaulStatusMapper.java
 */
public interface AnnaulStatusMapper {
	
	// 연차현황조회 - 다건조회
	List<AnnualLeaveVO> selectAnnualStatusList(AnnualLeaveVO param);
}
