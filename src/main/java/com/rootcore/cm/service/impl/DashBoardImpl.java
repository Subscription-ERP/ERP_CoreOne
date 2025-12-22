package com.rootcore.cm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.cm.mapper.DashBoardMapper;
import com.rootcore.cm.service.DashBoardService;
import com.rootcore.hr.vo.AnnualLeaveDetailVO;

import lombok.RequiredArgsConstructor;

/**
 * 대쉬보드 서비스 impl
 */
@Service
@RequiredArgsConstructor
public class DashBoardImpl implements DashBoardService{
	
	private final DashBoardMapper dashBoardMapper;

	/**
	 * 대쉬보드 휴가자 불러오기
	 */
	@Override
	public List<AnnualLeaveDetailVO> selectDashBoardAnnualLeaveDetail(AnnualLeaveDetailVO param) {
		// TODO Auto-generated method stub
		return dashBoardMapper.selectDashBoardAnnualLeaveDetail(param);
	}
	
}
