package com.rootcore.common.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SampleMapper {
	public String test();
	
	// 연결 테스트를 위한 주석
}
