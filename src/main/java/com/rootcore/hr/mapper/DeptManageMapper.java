package com.rootcore.hr.mapper;

import java.util.List;

import com.rootcore.hr.vo.DeptMasterVO;
import com.rootcore.hr.vo.UserVO;

public interface DeptManageMapper {
	
	// 부서조회
	List<DeptMasterVO> selectDeptList(DeptMasterVO param);
	
	// 사원조회
	List<UserVO> selectDeptUserList(DeptMasterVO param);

}
