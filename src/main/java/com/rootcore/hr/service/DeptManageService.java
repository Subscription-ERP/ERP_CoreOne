package com.rootcore.hr.service;

import java.util.List;

import com.rootcore.hr.vo.DeptMasterVO;
import com.rootcore.hr.vo.UserVO;

/**
 * 조직도 관리(DeptManage)와 관련된 로직을 정의하는 인터페이스입니다.
 * 
 * 주요기능으로는 부서조회(다건), 부서사원목록조회(다건), 사용자부서정보조회(단건)를 제공합니다.
 * 
 * @author 장준현
 * @version 1.0.0
 */
public interface DeptManageService {

	// 부서조회
	List<DeptMasterVO> selectDeptList(DeptMasterVO param);

	// 사원조회
	List<UserVO> selectDeptUserList(DeptMasterVO param);

	/**
	 * 현재 접속한 세션에 있는 회사코드와 사용자Id의 부서 단건 조회합니다.
	 * 
	 * @param param 검색조건을 담은 VO객체
	 * @return 조회된 사용자의 부서 이름(String)
	 */
	String findDeptNameByUserId(DeptMasterVO param);

}
