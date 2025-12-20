package com.rootcore.cm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.cm.mapper.ItemMasterMapper;
import com.rootcore.cm.service.ItemMasterService;
import com.rootcore.cm.vo.SkuVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemMasterServiceImpl implements ItemMasterService {

    private final ItemMasterMapper itemMasterMapper;

    @Override
    public List<SkuVO> search(SkuVO cond) {
        return itemMasterMapper.selectItemList(cond);
    }

    @Override
    public void save(SkuVO skuVO) {

        // 기본값 방어
        if (skuVO.getUseYn() == null) skuVO.setUseYn("Y");
        if (skuVO.getTaxYn() == null) skuVO.setTaxYn("Y");
        if (skuVO.getCreatedBy() == null) skuVO.setCreatedBy("ADMIN");
        skuVO.setUpdatedBy("ADMIN");

        int exists = itemMasterMapper.existsSku(
                skuVO.getCompanyCode(),
                skuVO.getSku()
        );

        if (exists > 0) {
            itemMasterMapper.updateSku(skuVO);   // 수정
        } else {
            itemMasterMapper.insertSku(skuVO);   // 신규
        }
    }

    @Override
    public String generateSku(String companyCode) {
        return itemMasterMapper.generateSku(companyCode);
    }
}
