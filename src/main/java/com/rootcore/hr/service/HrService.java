package com.rootcore.hr.service;

import java.util.List;

import com.rootcore.hr.vo.PayrollVO;
import com.rootcore.hr.vo.UserVO;

public interface HrService {

	// 사원
	List<UserVO> selectAllUserList();      // 전체조회

}
