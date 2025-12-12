package com.rootcore.cm.service;

import java.util.List;

import com.rootcore.hr.vo.DeptMasterVO;

/* ==================
 * 부서코드관리 인터페이스
 * ================== */
public interface DeptCodeManageService {
	
	/* ======
	 * 부서조회
	 * ====== */
	List<DeptMasterVO> selectDeptCodeManage(DeptMasterVO param);
	
	/* ===================
	 * 부서 등록 - 저장버튼기능
	 * =================== */
	String insertDeptCodeManage(DeptMasterVO param);
	
}
