package com.rootcore.hr.service;

import java.util.List;

import com.rootcore.hr.vo.AnnualLeaveDetailVO;
import com.rootcore.hr.vo.AnnualLeaveVO;

public interface AnnualManageService {

	// 연차조회
	List<AnnualLeaveDetailVO> selectAnnualManageList(AnnualLeaveDetailVO param);

	// 내연차현황
	AnnualLeaveVO selectmyAnnualStatus(AnnualLeaveVO param);
	
	// 연차신청
	int insertmyAnnualApply(AnnualLeaveDetailVO param);

}
