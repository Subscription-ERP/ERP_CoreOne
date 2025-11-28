package com.rootcore.sb.service;

import com.rootcore.sb.vo.CompanyRequestVO;

public interface CompanyService {
	/**
     * 회사정보를 등록하고 생성된 회사코드를 반환
     */
    String registerCompany(CompanyRequestVO requestVO);
}
