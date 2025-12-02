package com.rootcore.hr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.hr.mapper.PayrollMapper;
import com.rootcore.hr.service.PayrollService;
import com.rootcore.hr.vo.PayrollUserVO;
import com.rootcore.hr.vo.PayrollVO;
import com.rootcore.hr.vo.UserPayManageVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {
	private final PayrollMapper payrollMapper;

	// 급여대장-상여등록-사원조회
	@Override
	public List<PayrollUserVO> selectUserList(PayrollUserVO param) {
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
		
		// 신고 귀속 코드 조회
		String newPeriodCode = payrollMapper.selectNewPeriodCode();
		param.setPayrollPeriodCode(newPeriodCode);
		
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

	@Override
	public List<UserPayManageVO> selectUserPayManageList(String payroll_period_code) {

	    Map<String, Object> param = new HashMap<>();
	    param.put("payrollPeriodCode", payroll_period_code);
	    param.put("cursor", null); // 반드시 필요!
	    
	    System.out.println("serviceImpl payroll_period_code: " + payroll_period_code);
	    System.out.println("payrollMapper.selectUserPayManageList(param): " + payrollMapper.selectUserPayManageList(param));
	    
	    return payrollMapper.selectUserPayManageList(param);
	}
}
