package com.rootcore.sb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.sb.vo.CompanyVO;

@Mapper
public interface CompanyMapper {
	void insertCompany(CompanyVO company);

	CompanyVO selectCompany(String companyCode);
	
	 // 관리자용 회사 목록 조회
    List<CompanyVO> selectCompanyList(@Param("companyName") String companyName,
    	    @Param("ceoName") String ceoName);
    
    CompanyVO selectCompanyDetail(String companyCode);

    int updateCompany(CompanyVO vo);
    
    int countByBno(String bno);


}
