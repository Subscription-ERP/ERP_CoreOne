package com.rootcore.cm.mapper;

import java.util.List;

import com.rootcore.hr.vo.DeptMasterVO;

public interface DeptCodeManageMapper {
	
	/* =================
	 * 부서코드관리 부서조회
	 * ================= */
	List<DeptMasterVO> selectDeptCodeManage(DeptMasterVO param);
	
}
