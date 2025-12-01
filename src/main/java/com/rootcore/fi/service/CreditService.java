package com.rootcore.fi.service;

import java.util.List;

import com.rootcore.fi.vo.CreditVO;

public interface CreditService {

	public List<CreditVO> selectList(CreditVO credit);
	public int mergeCredit(CreditVO credit);
}
