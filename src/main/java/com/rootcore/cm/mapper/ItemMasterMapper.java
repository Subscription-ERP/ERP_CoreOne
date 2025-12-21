package com.rootcore.cm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.cm.vo.SkuVO;

@Mapper
public interface ItemMasterMapper {

    /** 품번 목록 조회 */
    List<SkuVO> selectItemList(SkuVO cond);

    /** 신규 품번 등록 */
    void insertSku(SkuVO skuVO);

    /** 품번 수정 */
    void updateSku(SkuVO skuVO);

    /** SKU 존재 여부 체크 */
    int existsSku(@Param("companyCode") String companyCode,
                  @Param("sku") String sku);

    /** SKU 자동생성 (S00001 ~ S99999 방식) */
    String generateSku(@Param("companyCode") String companyCode);
}
