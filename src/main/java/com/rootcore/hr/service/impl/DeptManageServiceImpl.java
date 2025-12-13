package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.hr.mapper.DeptManageMapper;
import com.rootcore.hr.service.DeptManageService;
import com.rootcore.hr.vo.DeptMasterVO;
import com.rootcore.hr.vo.UserVO;

import lombok.RequiredArgsConstructor;

/**
 * 조직도 관리(DeptManage)와 관련된 비즈니스 로직을 구현하는 Service Implementation입니다.
 * 
 * 부서 구조 조회 및 부서별 사원 목록 조회와 같은 핵심 업무 로직을 처리합니다.
 * 
 * @author 장준현
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class DeptManageServiceImpl implements DeptManageService {

	private final DeptManageMapper deptManageMapper;

	// 부서조회
	@Override
	public List<DeptMasterVO> selectDeptList(DeptMasterVO param) {
		return deptManageMapper.selectDeptList(param);
	}

	/**
	 * 특정 부서 코드를 기준으로 해당 부서에 속한 사원 목록을 조회합니다.
	 * 
	 * @param param 검색 조건(필터링 조건)을 담은 VO 객체
	 * @return 부서에 소속된 사원 목록 {@code List<userVO>}
	 */
	@Override
	public List<UserVO> selectDeptUserList(DeptMasterVO param) {
		return deptManageMapper.selectDeptUserList(param);
	}

	/**
	 * 현재 접속한 세션에 있는 회사코드와 사용자Id의 부서 단건 조회
	 * 
	 * @param param 검색조건을 담은 VO객체
	 * @return 조회된 사용자의 부서 이름(String)
	 */
	@Override
	public String findDeptNameByUserId(DeptMasterVO param) {
		return deptManageMapper.findDeptNameByUserId(param);
	}

}
