package com.rootcore.hr.service;

import java.util.List;

import com.rootcore.hr.vo.PayrollVO;
import com.rootcore.hr.vo.UserVO;

public interface HrService {
	//=================
	// 장준현
	//=================
	// 급여대장-상여등록-사원조회
	List<UserVO> selectUserList(UserVO param);
	// 급여대장-조회
	List<PayrollVO> selectPayrollList(PayrollVO param);
	
	//=================
	// 이한솔
	//=================
	List<UserVO> selectAllUserList();
	
}
