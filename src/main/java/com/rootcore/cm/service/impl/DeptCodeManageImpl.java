package com.rootcore.cm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.cm.mapper.DeptCodeManageMapper;
import com.rootcore.cm.service.DeptCodeManageService;
import com.rootcore.hr.vo.DeptMasterVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeptCodeManageImpl implements DeptCodeManageService {

	private final DeptCodeManageMapper deptCodeManageMapper;

	/*
	 * 부서코드관리 부서조회
	 */
	@Override
	public List<DeptMasterVO> selectDeptCodeManage(DeptMasterVO param) {
		return deptCodeManageMapper.selectDeptCodeManage(param);
	}

	/*
	 * 부서 등록 - 저장버튼기능
	 */
	@Override
	@Transactional
	public String insertDeptCodeManage(DeptMasterVO param) {
		int resultNumber = 0;
		String result;
		if (param.getDeptCode() != null) {
			resultNumber = deptCodeManageMapper.updateDeptCodeManage(param);
			deptCodeManageMapper.updateDescendantLevels(param);
			result = resultNumber > 0 ? "수정완료" : "실패"; 
			return result;
		} else {
			resultNumber = deptCodeManageMapper.insertDeptCodeManage(param);
			result = resultNumber > 0 ? "등록완료" : "실패";
			return result;
		}
	}
}
