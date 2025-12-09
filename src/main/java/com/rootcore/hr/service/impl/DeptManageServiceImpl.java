package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.hr.mapper.DeptManageMapper;
import com.rootcore.hr.service.DeptManageService;
import com.rootcore.hr.vo.DeptMasterVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeptManageServiceImpl implements DeptManageService {

	private final DeptManageMapper deptManageMapper;

	// 부서조회
	@Override
	public List<DeptMasterVO> selectDeptList(DeptMasterVO param) {
		return deptManageMapper.selectDeptList(param);
	}

}
