package com.rootcore.cm.service;

import java.util.List;

import com.rootcore.hr.vo.AnnualLeaveDetailVO;

/**
 * 대쉬보드 서비스
 */
public interface DashBoardService {
	
	/**
	 * 오늘의 휴가자 불러오는 
	 */
	List<AnnualLeaveDetailVO> selectDashBoardAnnualLeaveDetail(AnnualLeaveDetailVO param);
}
