package com.rootcore.hr.service;

import java.util.List;

import com.rootcore.hr.vo.UserPayManageVO;

public interface PayrollManageService {

	// 급여관리-사원급여조회
	List<UserPayManageVO> selectPayrollManageList(UserPayManageVO param);

}
