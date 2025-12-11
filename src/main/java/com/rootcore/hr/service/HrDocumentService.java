package com.rootcore.hr.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rootcore.hr.vo.UserVO;

public interface HrDocumentService {

	// 사원조회 및 검색
	List<UserVO> selectUser(@Param("userName") String userName,
			                @Param("userId") String userId);
	
}
