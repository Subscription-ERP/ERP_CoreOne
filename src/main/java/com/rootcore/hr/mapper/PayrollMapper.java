package com.rootcore.hr.mapper;

import java.util.List;

import com.rootcore.hr.vo.PayrollUserVO;
import com.rootcore.hr.vo.PayrollVO;

public interface PayrollMapper {
	
	// 급여대장-상여등록-사원조회
	List<PayrollUserVO> selectUserList(PayrollUserVO param);

	// 급여대장조회
	List<PayrollVO> selectPayrollList(PayrollVO param);
	
	// 급여대장-상여등록
	int insertBonusPayrollOne(PayrollVO param);
}
