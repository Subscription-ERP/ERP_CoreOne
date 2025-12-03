package com.rootcore.hr.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rootcore.hr.vo.DeptVO;
import com.rootcore.hr.vo.UserSearchVO;
import com.rootcore.hr.vo.UserVO;

public interface HrService {

	// 사원
	List<UserVO> selectAllUserList();                                // 전체조회
	UserVO selectUserDetail(String userId);                          // 상세조회(기본사항,자격증,경력사항,이력)
	List<DeptVO> selectDeptMaster();                                 // 부서조회
	List<UserVO> selectUserSearch(UserSearchVO userSearchVO);        // 검색
	int insertUser(UserVO userVO, 
			       MultipartFile userPhoto,
			       MultipartFile userFile,
			       List<MultipartFile> certiFiles) throws Exception; // 등록
	int updateUser(UserVO userVO, 
			       MultipartFile userPhoto,
			       MultipartFile userFile,
			       List<MultipartFile> certiFiles) throws Exception; // 수정
	
}
