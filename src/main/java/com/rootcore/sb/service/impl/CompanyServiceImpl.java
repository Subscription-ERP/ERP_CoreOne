package com.rootcore.sb.service.impl;

import java.time.LocalDateTime;
import java.util.List;

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
	    public String registerCompany(CompanyVO company) {


	        // ❌ 더 이상 회사코드 직접 생성 X
	        // company.setCompanyCode(generateCompanyCode());
	        // ⬇️ DB에서 자동 생성되게 비워둔다 (null)
	        // 필요 없으면 아예 setCompanyCode 호출 안 해도 됨



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
	public CompanyVO selectCompany(String companyCode) {
		return companyMapper.selectCompany(companyCode);
	}

	@Override
	public List<CompanyVO> selectCompanyList(String companyName, String ceoName) {
		  return companyMapper.selectCompanyList(companyName,ceoName);
	}

	@Override
	public CompanyVO selectCompanyDetail(String companyCode) {
		return companyMapper.selectCompanyDetail(companyCode);
	}

	@Override
	public int updateCompany(CompanyVO vo) {
		return companyMapper.updateCompany(vo);
	}
	  
	  
}
