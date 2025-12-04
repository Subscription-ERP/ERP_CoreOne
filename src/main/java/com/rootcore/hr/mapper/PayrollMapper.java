package com.rootcore.hr.mapper;

import java.util.List;
import java.util.Map;

import com.rootcore.hr.vo.PayrollUserVO;
import com.rootcore.hr.vo.PayrollVO;
import com.rootcore.hr.vo.UserPayManageVO;

public interface PayrollMapper {
	
	// 급여대장-상여등록-사원조회
	List<PayrollUserVO> selectUserList(PayrollUserVO param);

	// 급여대장조회
	List<PayrollVO> selectPayrollList(PayrollVO param);
	
	// 급여대장-상여등록
	int insertBonusPayrollOne(PayrollVO param);
	
	// 신고귀속코드
	String selectNewPeriodCode();
	
	// 급여대장-대장조회-계산하기-급여계산
	List<UserPayManageVO> selectUserPayManageList(Map<String, Object> map);

	// 급여대장-계산하기모달창-확정버튼
	int insertUserPay(UserPayManageVO payVO);

	// 급여대장-계산하기-급여관리모달창-확정버튼-이미 있는 데이터인지 확인하는 select
	int checkUserPay(String payrollCode);
}
