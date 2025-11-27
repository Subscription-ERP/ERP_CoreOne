package com.rootcore.fi.service;

import java.util.List;

import com.rootcore.fi.vo.UnitPriceVO;

public interface UnitPriceService {

	public List<UnitPriceVO> selectList(UnitPriceVO unitPrice);
	public int insertUnitPrice(UnitPriceVO unitPrice);
	public int checkUnitPrice(UnitPriceVO unitPrice);
}
