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

    /**
     * 품번 조회
     */
    @Override
    public List<SkuVO> search(SkuVO cond) {
        return itemMasterMapper.selectItemList(cond);
    }

    /**
     * 신규 품번 등록
     */
    @Override
    public void insertSku(SkuVO skuVO) {
        itemMasterMapper.insertSku(skuVO);
    }

    /**
     * 품번 자동생성
     * 규칙: [SKU_TYPE]-[00001]
     */
    @Override
    public String generateSku(String companyCode, String skuType) {

        String maxSku = itemMasterMapper.selectMaxSku(companyCode, skuType);

        int nextSeq = 1;
        if (maxSku != null && maxSku.length() >= 5) {
            String num = maxSku.substring(maxSku.length() - 5);
            nextSeq = Integer.parseInt(num) + 1;
        }

        return skuType + "-" + String.format("%05d", nextSeq);
    }
}
