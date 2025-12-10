package com.rootcore.cm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.cm.mapper.DeptCodeManageMapper;
import com.rootcore.cm.service.DeptCodeManageService;
import com.rootcore.hr.vo.DeptMasterVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeptCodeManageImpl implements DeptCodeManageService {

	private final DeptCodeManageMapper deptCodeManageMapper; 
	
	/* =================
	 * 부서코드관리 부서조회
	 * ================= */
	@Override
	public List<DeptMasterVO> selectDeptCodeManage(DeptMasterVO param) {
		return deptCodeManageMapper.selectDeptCodeManage(param);
	}

}
