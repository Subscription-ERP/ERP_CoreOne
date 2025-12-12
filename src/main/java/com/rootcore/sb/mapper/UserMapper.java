package com.rootcore.sb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.sb.vo.SbLoginVO;
import com.rootcore.sb.vo.SbUserVO;

@Mapper
public interface UserMapper {
	void insertUser(SbUserVO user);

	void insertLogin(SbLoginVO login);

	
	// 회사 관리자(ADMIN) 계정 수 조회
    int countCompanyManager(@Param("companyCode") String companyCode);
    
    void insertRoleMenu(@Param("companyCode") String companyCode);
}
