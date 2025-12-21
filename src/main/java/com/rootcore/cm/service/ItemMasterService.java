package com.rootcore.cm.service;

import java.util.List;
import com.rootcore.cm.vo.SkuVO;

public interface ItemMasterService {

    /** 품번 조회 */
    List<SkuVO> search(SkuVO cond);

    /** 저장 (신규 / 수정 공용) */
    void save(SkuVO skuVO);

    /** 품번 자동생성 (S00001 방식) */
    String generateSku(String companyCode);
}
