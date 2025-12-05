package com.rootcore.sb.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rootcore.sb.vo.CompanyVO;

public interface CompanyService {
	/**
     * 회사정보를 등록하고 생성된 회사코드를 반환
     */
    String registerCompany(CompanyVO requestVO);
    
    CompanyVO selectCompany(String companyCode);
    List<CompanyVO> selectCompanyList(@Param("companyName") String companyName,
    	    @Param("ceoName") String ceoName);
}
