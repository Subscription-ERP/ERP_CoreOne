package com.rootcore.cm.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SkuVO {
    private String companyCode;     // 회사코드
    private String sku;             // 품목코드
    private String skuName;         // 품목명
    private String skuType;         // 품목분류
    private String spec;            // 규격
    private String unit;            // 단위
    private String remark;          // 비고
    private String useYn;           // 사용여부
    private String taxYn;           // 과세여부
    private String createdBy;       // 생성자
    private Date createDate;        // 생성일자
    private String updatedBy;       // 수정자
    private Date updateDate;        // 수정일자
}
