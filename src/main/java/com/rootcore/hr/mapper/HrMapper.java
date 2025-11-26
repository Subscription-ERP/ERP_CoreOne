package com.rootcore.hr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.rootcore.hr.vo.UserVO;

@Mapper
public interface HrMapper {
	//=================
	// 장준현
	//=================
	// 급여대장-상여등록-사원조회
	List<UserVO> selectUserList(UserVO param);
	
	//=================
	// 이한솔
	//=================
	List<UserVO> selectAllUserList();
	
	
	
	
}
