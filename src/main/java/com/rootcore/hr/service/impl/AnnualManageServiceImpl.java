package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.hr.mapper.AnnualManageMapper;
import com.rootcore.hr.service.AnnualManageService;
import com.rootcore.hr.vo.AnnualLeaveDetailVO;
import com.rootcore.hr.vo.AnnualLeaveVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnnualManageServiceImpl implements AnnualManageService {

	private final AnnualManageMapper annualManageMapper;
	
	// 연차조회
	@Override
	public List<AnnualLeaveDetailVO> selectAnnualManageList(AnnualLeaveDetailVO param) {
		return annualManageMapper.selectAnnualManageList(param);
	}

	// 연차현황
	@Override
	public AnnualLeaveVO selectmyAnnualStatus(AnnualLeaveVO param) {
		return annualManageMapper.selectmyAnnualStatus(param);
	}

	// 연차신청
	@Override
	@Transactional
	public int insertmyAnnualApply(AnnualLeaveDetailVO param) {
		System.out.println(">>> 연차신청 param: " + param);
		int totalSuccess = 0;
		
		// 연차신청하면 연차상세관리 테이블에 등록되고
		totalSuccess += annualManageMapper.insertmyAnnualApply(param);
		
		// 연차관리 테이블은 update가 되야하고(잔여연차, 총사용일수)
		totalSuccess += annualManageMapper.updateAnnualApply(param);
		
		// 근태테이블에 insert해야한다
		totalSuccess += annualManageMapper.insertAttendance(param);
		
		return totalSuccess;
	}

}
