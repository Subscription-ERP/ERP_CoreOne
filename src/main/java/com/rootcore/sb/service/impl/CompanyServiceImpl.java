package com.rootcore.sb.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.sb.mapper.CompanyMapper;
import com.rootcore.sb.service.CompanyService;
import com.rootcore.sb.vo.CompanyVO;

@Service
public class CompanyServiceImpl implements CompanyService {
	  private final CompanyMapper companyMapper;
	  
	  public CompanyServiceImpl(CompanyMapper companyMapper) {
	        this.companyMapper = companyMapper;
	    }
	  
	  @Override
	    @Transactional
	    public String registerCompany(CompanyVO requestVO) {

	        CompanyVO company = new CompanyVO();

	        // ❌ 더 이상 회사코드 직접 생성 X
	        // company.setCompanyCode(generateCompanyCode());
	        // ⬇️ DB에서 자동 생성되게 비워둔다 (null)
	        // 필요 없으면 아예 setCompanyCode 호출 안 해도 됨

	        company.setCompanyName(requestVO.getCompanyName());
	        company.setCeoName(requestVO.getCeoName());
	        company.setCeoPhone(requestVO.getCeoPhone());
	        company.setCompanyEmail(requestVO.getCompanyEmail());
	        company.setIndustryType(requestVO.getIndustryType());
	        company.setBusinessType(requestVO.getBusinessType());
	        company.setManagerName(requestVO.getManagerName());
	        company.setEmployeeCount(requestVO.getEmployeeCount());
	        company.setBno(requestVO.getBno());
	        company.setCheckNo(requestVO.getCheckNo());
	        company.setCompanyAddress(requestVO.getCompanyAddress());
	        company.setCompanyPhone(requestVO.getCompanyPhone());
	        company.setManagerPhone(requestVO.getManagerPhone());

	        company.setCreatedBy("SYSTEM");
	        company.setCreateDate(LocalDateTime.now());
	        company.setUpdatedBy("SYSTEM");
	        company.setUpdateDate(LocalDateTime.now());

	        // 🔹 여기서 DB INSERT + PK(회사코드) 자동생성
	        companyMapper.insertCompany(company);

	        // 🔹 MyBatis <selectKey> 설정이 되어 있다면,
	        //     company.setCompanyCode(...) 가 자동으로 채워진 상태가 된다.
	        return company.getCompanyCode();
	    }
	  @Override
	  public CompanyVO getCompany(String companyCode) {
	      return companyMapper.selectCompany(companyCode);
	  }
	  
}
