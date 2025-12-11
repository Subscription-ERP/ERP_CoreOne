package com.rootcore.hr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.hr.vo.UserVO;

@Mapper
public interface HrDocumentMapper {

	// 사원조회 및 검색
	List<UserVO> selectUser(@Param("userName") String userName,
			                @Param("userId") String userId);
	
	
	
	
	
	
	
	
	
}
