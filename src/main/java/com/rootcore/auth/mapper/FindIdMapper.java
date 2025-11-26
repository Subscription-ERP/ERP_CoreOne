package com.rootcore.auth.mapper;



	import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.rootcore.auth.vo.LoginFindIdVO;

	@Mapper
	public interface FindIdMapper {
	
	    // 이메일로 아이디(사원번호) 찾기
	    List<LoginFindIdVO> findIdByEmail(String email);
	}


