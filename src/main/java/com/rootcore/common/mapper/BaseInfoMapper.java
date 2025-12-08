package com.rootcore.common.mapper;

import org.apache.ibatis.annotations.Mapper;   // ★ MyBatis Mapper 선언

import com.rootcore.common.vo.BaseInfoVO;

@Mapper
public interface BaseInfoMapper {
	BaseInfoVO getCompany(String companyCode);
}