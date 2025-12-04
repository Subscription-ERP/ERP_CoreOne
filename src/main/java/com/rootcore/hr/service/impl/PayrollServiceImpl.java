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
	
	// 급여대장-계산하기-모달창-계산된내용조회
	@Override
	public List<UserPayManageVO> selectUserPayManageList(Map<String, Object> map) {

		payrollMapper.selectUserPayManageList(map);

		@SuppressWarnings("unchecked")
		List<UserPayManageVO> UserPayManage = (List<UserPayManageVO>)map.get("key");
	    
		System.out.println("serviceImpl payrollPeriodCode: " + map.get("payrollPeriodCode"));
		System.out.println("UserPayManage: " + UserPayManage);
	    
	    return UserPayManage;
	}
	
	// 급여대장-계산하기모달창-확정버튼
	@Override
	@Transactional 
	public int insertUserPay(List<UserPayManageVO> userPayManageList) {
		int totalSuccessCount = 0;
		for (UserPayManageVO payVO : userPayManageList ) {
			int insertedRows = payrollMapper.insertUserPay(payVO);
			totalSuccessCount += insertedRows; 
		}
		return totalSuccessCount;
	}

	// 급여대장-계산하기-급여관리모달창-확정버튼-이미 있는 데이터인지 확인하는 select
	@Override
	public int checkUserPay(String payrollCode) {
		return payrollMapper.checkUserPay(payrollCode);
	}
}
