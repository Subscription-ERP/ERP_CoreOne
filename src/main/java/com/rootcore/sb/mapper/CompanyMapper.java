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

	
	 // (선택) 상태 업데이트 같은 거 쓰고 싶으면 나중에 추가
    // void updateCompanyStatus(@Param("companyCode") String companyCode,
    //                          @Param("status") String status,
    //                          @Param("updatedBy") String updatedBy);
}
