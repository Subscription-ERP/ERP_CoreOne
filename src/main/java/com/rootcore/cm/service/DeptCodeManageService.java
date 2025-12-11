package com.rootcore.cm.service;

import java.util.List;

import com.rootcore.hr.vo.DeptMasterVO;

public interface DeptCodeManageService {
	
	/* =================
	 * 부서코드관리 부서조회
	 * ================= */
	List<DeptMasterVO> selectDeptCodeManage(DeptMasterVO param);
	
}
