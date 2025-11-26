package com.rootcore.fi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rootcore.fi.mapper.UnitPriceMapper;
import com.rootcore.fi.service.UnitPriceService;
import com.rootcore.fi.vo.UnitPriceVO;

@Service
public class UnitPriceServiceImpl implements UnitPriceService{

	@Autowired UnitPriceMapper unitPriceMapper;
	@Override
	public List<UnitPriceVO> selectList(UnitPriceVO unitPrice) {
		
		return unitPriceMapper.selectUnitPrice(unitPrice);
	}
	@Override
	public int insertUnitPrice(UnitPriceVO unitPrice) {
		// TODO Auto-generated method stub
		return unitPriceMapper.insertUnitPrice(unitPrice);
	}

}
