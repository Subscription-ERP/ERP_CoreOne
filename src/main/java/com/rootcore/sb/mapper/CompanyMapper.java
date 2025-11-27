package com.rootcore.sb.mapper;

import com.rootcore.sb.domain.Company;

public interface CompanyMapper {
	void insertCompany(Company company);

	Company selectByCompanyCode(String companyCode);
}
