package com.rootcore.hr.service;

import java.util.List;

import com.rootcore.hr.vo.DeptVO;
import com.rootcore.hr.vo.UserSearchVO;
import com.rootcore.hr.vo.UserVO;

public interface HrService {

	// 사원
	List<UserVO> selectAllUserList();                                // 전체조회
	UserVO selectUserDetail(String userId);                          // 상세조회(기본사항,자격증,경력사항,이력)
	List<DeptVO> selectDeptMaster();                                 // 부서조회
	byte[] userCardPdf(String userId) throws Exception;              // 사원카드PDF
	List<UserVO> selectUserSearch(UserSearchVO userSearchVO);        // 검색
	
}
