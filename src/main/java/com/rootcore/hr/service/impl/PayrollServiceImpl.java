package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	// 급여대장조회
	@Override
	public List<PayrollVO> selectPayrollList(PayrollVO param) {
		return payrollMapper.selectPayrollList(param);
	}

	// 급여대장-상여등록
	@Override
	@Transactional // 트랜잭션 필수: 하나라도 실패하면 전체 롤백
	public int insertBonusPayroll(PayrollVO param) {
		int count = 0;
		List<String> targetEmployees = param.getEmployeeIds();

		// 리스트가 비어있지 않다면 반복문 실행
		if (targetEmployees != null && !targetEmployees.isEmpty()) {
			for (String empId : targetEmployees) {
				// 1. 현재 순서의 사원 ID를 VO에 세팅
				param.setUserId(empId);

				// 2. 단건 Insert 실행
				count += payrollMapper.insertBonusPayrollOne(param);
			}
		}
		return count; // 총 등록된 건수 반환
	}
}
