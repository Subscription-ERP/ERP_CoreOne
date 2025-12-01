package com.rootcore.hr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.rootcore.hr.vo.CertificationVO;
import com.rootcore.hr.vo.DeptVO;
import com.rootcore.hr.vo.UserHistoryVO;
import com.rootcore.hr.vo.UserSearchVO;
import com.rootcore.hr.vo.UserVO;
import com.rootcore.hr.vo.WorkExperienceVO;

@Mapper
public interface HrMapper {

	// 사원
	List<UserVO> selectAllUserList();                                // 전체조회
	UserVO selectUserDetail(String userId);                          // 상세조회 - 기본사항
	List<CertificationVO> selectUserCertification(String userId);    // 상세조회 - 자격증
	List<WorkExperienceVO> selectUserWorkExperience(String userId);  // 상세조회 - 경력사항
	List<UserHistoryVO> selectUserHistory(String userId);            // 상세조회 - 이력
	List<DeptVO> selectDeptMaster();                                 // 부서조회
	List<UserVO> selectUserSearch(UserSearchVO userSearchVO);        // 검색

}
