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
		int insert = unitPriceMapper.insertUnitPrice(unitPrice);
		int history = unitPriceMapper.insertUnitPriceHistory(unitPrice);
		return (insert > 0 || history > 0) ? 0 : insert;
	}
	@Override
	public int checkUnitPrice(UnitPriceVO unitPrice) {
		// TODO Auto-generated method stub
		return unitPriceMapper.checkUnitPrice(unitPrice);
	}
	public int updateUnitPrice(UnitPriceVO unitPrice) {

		int update = unitPriceMapper.updateUnitPrice(unitPrice);
		int history = unitPriceMapper.insertUnitPriceHistory(unitPrice);
		return (update > 0 || history > 0) ? 0 : update;
	}

}
