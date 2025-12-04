package com.rootcore.cm.vo;

import lombok.Data;

@Data
public class SkuVO {
    private String sku;         // 품목코드
    private String skuName;     // 품목명
    private String skuType;     // 품목타입
    private String spec;        // 규격
    private String unit;        // 단위
    private String remark;      // 비고
    private String unitPriceType;   // 단가유형코드
    private String typeName;        // 단가유형이름
    private int unitPrice;          // 단가
}
