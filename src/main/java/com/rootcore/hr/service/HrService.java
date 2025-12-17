package com.rootcore.hr.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rootcore.hr.vo.DeptVO;
import com.rootcore.hr.vo.UserSearchVO;
import com.rootcore.hr.vo.UserVO;

public interface HrService {

	// 사원
	List<UserVO> selectUserList(UserSearchVO userSearchVO);          // 전체조회 + 검색
	
	UserVO selectUserDetail(String userId);                          // 상세조회(기본사항,자격증,경력사항,이력)
	List<DeptVO> selectDeptMaster();                                 // 부서조회
	
	int insertUser(UserVO userVO, 
			       MultipartFile userPhoto,
			       MultipartFile userFile,
			       List<MultipartFile> certiFiles) throws Exception; // 등록
	int updateUser(UserVO userVO, 
			       MultipartFile userPhoto,
			       MultipartFile userFile,
			       List<MultipartFile> certiFiles) throws Exception; // 수정
	
	// 비밀번호 초기화 링크 재발송
    boolean resendResetLink(String companyCode, String userId, String requestedBy);
	
}
