package com.rootcore.cm.mapper;

import com.rootcore.cm.vo.SkuVO;

import java.util.List;

public interface SkuMapper {
    List<SkuVO> selectAllSku(SkuVO sku);
}
