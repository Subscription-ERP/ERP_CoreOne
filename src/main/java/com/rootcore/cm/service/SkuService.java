package com.rootcore.cm.service;

import com.rootcore.cm.vo.SkuVO;

import java.util.List;

public interface SkuService {
    List<SkuVO> getInOrdSkuList(String custCode);
    List<SkuVO> getSku(String unitPriceType, String sku, String skuName);
}
