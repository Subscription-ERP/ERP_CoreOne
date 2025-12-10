package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.hr.mapper.PayrollManageMapper;
import com.rootcore.hr.service.PayrollManageService;
import com.rootcore.hr.vo.PayrollManageSearchVO;
import com.rootcore.hr.vo.UserPayManageDetailVO;
import com.rootcore.hr.vo.UserPayManageVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollManageServiceImpl implements PayrollManageService {
	private final PayrollManageMapper payrollManageMapper;

	// 급여관리-사원급여조회
	@Override
	public List<UserPayManageVO> selectPayrollManageList(PayrollManageSearchVO param) {
		List<UserPayManageVO> list = payrollManageMapper.selectPayrollManageList(param);
		return list;
	}

	// 단건조회
	@Override
	public UserPayManageDetailVO selectPayrollManageDetail(String userPayManagementCode) {
		return payrollManageMapper.selectPayrollManageDetail(userPayManagementCode);
	}
}
