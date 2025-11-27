package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.hr.mapper.PayrollMapper;
import com.rootcore.hr.service.PayrollService;
import com.rootcore.hr.vo.PayrollVO;
import com.rootcore.hr.vo.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {
	private final PayrollMapper payrollMapper;

	// 급여대장-상여등록-사원조회
	@Override
	public List<UserVO> selectUserList(UserVO param) {
		return payrollMapper.selectUserList(param);
	}

	// 사원 - 사원전체조회
	@Override
	public List<PayrollVO> selectPayrollList(PayrollVO param) {
		return payrollMapper.selectPayrollList(param);
	}
}
