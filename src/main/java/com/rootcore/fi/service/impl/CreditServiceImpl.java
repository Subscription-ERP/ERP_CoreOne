package com.rootcore.fi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rootcore.fi.mapper.CreditMapper;
import com.rootcore.fi.service.CreditService;
import com.rootcore.fi.vo.CreditVO;

@Service
public class CreditServiceImpl implements CreditService{
	@Autowired CreditMapper creditMapper;

	@Override
	public List<CreditVO> selectList(CreditVO credit) {
		return creditMapper.selectCredit(credit);
	}

	@Override
	public int mergeCredit(CreditVO credit) {
		// TODO Auto-generated method stub
		return creditMapper.mergeCredit(credit);
	}

}
