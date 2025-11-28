package com.rootcore.hr.service;

import java.util.List;

import com.rootcore.hr.vo.CertificationVO;
import com.rootcore.hr.vo.DeptVO;
import com.rootcore.hr.vo.UserHistoryVO;
import com.rootcore.hr.vo.UserVO;
import com.rootcore.hr.vo.WorkExperienceVO;

public interface HrService {

	// 사원
	List<UserVO> selectAllUserList();                                // 전체조회
	UserVO selectUserDetail(String userId);                          // 상세조회(기본사항,자격증,경력사항,이력)
	List<DeptVO> selectDeptMaster();                                 // 부서조회

}
