package com.rootcore.hr.mapper;

import java.util.List;

import com.rootcore.hr.vo.DeptMasterVO;

public interface DeptManageMapper {
	
	// 부서조회
	List<DeptMasterVO> selectDeptList(DeptMasterVO param);

}
