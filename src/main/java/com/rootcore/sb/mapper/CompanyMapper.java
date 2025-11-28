package com.rootcore.sb.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.rootcore.sb.domain.Company;

@Mapper
public interface CompanyMapper {
	void insertCompany(Company company);

	Company selectByCompanyCode(String companyCode);
	
	 // (선택) 상태 업데이트 같은 거 쓰고 싶으면 나중에 추가
    // void updateCompanyStatus(@Param("companyCode") String companyCode,
    //                          @Param("status") String status,
    //                          @Param("updatedBy") String updatedBy);
}
