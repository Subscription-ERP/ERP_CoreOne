package com.rootcore.hr.mapper;

import java.util.List;

import com.rootcore.hr.vo.UserPayManageVO;

public interface PayrollManageMapper {
	
	// 급여관리-사원급여조회
	List<UserPayManageVO> selectPayrollManageList(UserPayManageVO param);
}
