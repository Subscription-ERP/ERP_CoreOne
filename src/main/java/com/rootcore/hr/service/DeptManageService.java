package com.rootcore.hr.service;

import java.util.List;

import com.rootcore.hr.vo.DeptMasterVO;
import com.rootcore.hr.vo.UserVO;

public interface DeptManageService {

	// 부서조회
	List<DeptMasterVO> selectDeptList(DeptMasterVO param);

	// 사원조회
	List<UserVO> selectDeptUserList(DeptMasterVO param);

}
