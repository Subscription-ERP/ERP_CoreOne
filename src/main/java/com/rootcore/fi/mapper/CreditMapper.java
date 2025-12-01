package com.rootcore.fi.mapper;

import java.util.List;

import com.rootcore.fi.vo.CreditVO;

public interface CreditMapper {

	List<CreditVO> selectCredit(CreditVO credit);
	int mergeCredit(CreditVO credit);
}
