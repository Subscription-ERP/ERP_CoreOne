package com.rootcore.cm.service;

import com.rootcore.cm.vo.SkuVO;

import java.util.List;

public interface SkuService {
    List<SkuVO> getSku(String custCode, String unitPriceType, String sku, String skuName);
    List<SkuVO> getInOrdSkuList(String custCode);
}
