package com.rootcore.cm.mapper;

import com.rootcore.cm.vo.SkuVO;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkuMapper {
    List<SkuVO> selectSku(@Param("custCode") String custCode,
                          @Param("unitPriceType") String unitPriceType,
                          @Param("sku") String sku,
                          @Param("skuName") String skuName);

} 
