package com.rootcore.cm.service.impl;

import com.rootcore.cm.mapper.SkuMapper;
import com.rootcore.cm.service.SkuService;
import com.rootcore.cm.vo.SkuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SkuService")
@RequiredArgsConstructor
public class SkuServiceImpl implements SkuService {

    final SkuMapper skuMapper;

    @Override
    public List<SkuVO> getSku(String custCode, String unitPriceType, String sku, String skuName) {
        return skuMapper.selectSku(custCode, unitPriceType, sku, skuName);
    }

    @Override
    public List<SkuVO> getInOrdSkuList(String custCode) {
        return skuMapper.selectInOrdSku(custCode);
    }

}
