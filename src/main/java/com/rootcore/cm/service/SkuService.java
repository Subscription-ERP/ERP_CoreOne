package com.rootcore.cm.service;

import com.rootcore.cm.vo.SkuVO;

import java.util.List;

public interface SkuService {
    List<SkuVO> getSkuList(SkuVO sku);
    List<SkuVO> getInOrdSkuList(String custCode);
    List<SkuVO> getOneSku(SkuVO sku);
}
