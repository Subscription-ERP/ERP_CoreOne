package com.rootcore.cm.mapper;

import java.util.List;

import com.rootcore.hr.vo.AnnualLeaveDetailVO;

public interface DashBoardMapper {

	/**
	 * 오늘의 휴가자 불러오는 매퍼
	 * @param 오늘날짜, 회사코드
	 * @return List<AnnualLeaveDetailVO>
	 */
	List<AnnualLeaveDetailVO> selectDashBoardAnnualLeaveDetail(AnnualLeaveDetailVO param);

}
