package com.rootcore.hr.service;

import java.util.List;
import java.util.Map;

import com.rootcore.hr.vo.PayrollUserVO;
import com.rootcore.hr.vo.PayrollVO;
import com.rootcore.hr.vo.UserPayManageVO;

public interface PayrollService {

	// 급여대장-상여등록-사원조회
	List<PayrollUserVO> selectUserList(PayrollUserVO param);

	// 급여대장-조회
	List<PayrollVO> selectPayrollList(PayrollVO param);

	// 급여대장-상여등록
	int insertBonusPayroll(PayrollVO param);
	
	// 급여대장-대장조회-계산하기-급여계산
	List<UserPayManageVO> selectUserPayManageList(Map<String, Object> map);
	
	// 급여대장-계산하기모달창-확정버튼
	int insertUserPay(List<UserPayManageVO> userPayManageList);

}
