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

    /** 품번 자동생성용 MAX 조회 */
    String selectMaxSku(@Param("companyCode") String companyCode,
                        @Param("skuType") String skuType);
}
