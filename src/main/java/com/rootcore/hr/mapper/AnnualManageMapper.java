package com.rootcore.hr.mapper;

import java.util.List;

import com.rootcore.hr.vo.AnnualLeaveDetailVO;
import com.rootcore.hr.vo.AnnualLeaveVO;

public interface AnnualManageMapper {

	// 사원연차신청이력조회
	List<AnnualLeaveDetailVO> selectAnnualManageList(AnnualLeaveDetailVO param);
	
	// 연차현황
	AnnualLeaveVO selectmyAnnualStatus(AnnualLeaveVO param);

	// 연차신청
	int insertmyAnnualApply(AnnualLeaveDetailVO param);
	
	// 연차신청시 연차관리테이블 업데이트
	int updateAnnualApply(AnnualLeaveDetailVO param);
	
	// 연차신청시 근태테이블에 출퇴근기록 insert
	int insertAttendance(AnnualLeaveDetailVO param);

}
