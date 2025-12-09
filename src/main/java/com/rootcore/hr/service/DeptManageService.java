package com.rootcore.hr.service;

import java.util.List;

import com.rootcore.hr.vo.DeptMasterVO;

public interface DeptManageService {

	// 부서조회
	List<DeptMasterVO> selectDeptList(DeptMasterVO param);


}
