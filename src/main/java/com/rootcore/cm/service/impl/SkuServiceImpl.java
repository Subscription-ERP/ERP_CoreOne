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
    public List<SkuVO> getSkuList(SkuVO sku) {
        return skuMapper.selectAllSku(sku);
    }
}
