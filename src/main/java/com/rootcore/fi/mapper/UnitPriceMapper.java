package com.rootcore.fi.mapper;

import java.util.List;

import com.rootcore.fi.vo.UnitPriceVO;

public interface UnitPriceMapper {

	List<UnitPriceVO> selectUnitPrice(UnitPriceVO unitPrice);
	int insertUnitPrice(UnitPriceVO unitPrice);
	int checkUnitPrice(UnitPriceVO unitPrice);
}
