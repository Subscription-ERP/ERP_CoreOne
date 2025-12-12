package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.hr.mapper.AnnaulStatusMapper;
import com.rootcore.hr.service.AnnualStatusService;
import com.rootcore.hr.vo.AnnualLeaveVO;

import lombok.RequiredArgsConstructor;
/*
 * 연차현황조회 Impl
 * AnnualStatusServiceImpl.java
 */
@Service
@RequiredArgsConstructor
public class AnnualStatusServiceImpl implements AnnualStatusService{
	private final AnnaulStatusMapper annaulStatusMapper;

	// 연차현황조회 - 다건조회
	@Override
	public List<AnnualLeaveVO> selectAnnualStatusList(AnnualLeaveVO param) {
		return annaulStatusMapper.selectAnnualStatusList(param);
	}

}
