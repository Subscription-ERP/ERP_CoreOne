package com.rootcore.cm.service;

import java.util.List;

import com.rootcore.cm.vo.SkuVO;

public interface ItemMasterService {

    /** 품번 조회 */
    List<SkuVO> search(SkuVO cond);

    /** 신규 품번 등록 */
    void insertSku(SkuVO skuVO);

    /** 품번 자동생성 */
    String generateSku(String companyCode, String skuType);    
}
