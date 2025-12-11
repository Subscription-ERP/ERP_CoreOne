package com.rootcore.sb.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ContractVO {
	
	private String contractCode;
    private String companyCode;
    private LocalDateTime contractDate;

    private String companyName;
    private String ceoName;
    private String signName;
    private String filePath;

    private String planCode;

    private LocalDateTime contractStart;
    private LocalDateTime contractEnd;

    private Integer subsPeriod;
    private Integer userCount;
    private Integer billingPeriod;
    
    private BigDecimal totalPrice;
    private BigDecimal vat;
    private BigDecimal discountAmount;
    private BigDecimal baseTotal;

    private String status;

    private String createdBy;
    private LocalDateTime createDate;
    private String updatedBy;
    private LocalDateTime updateDate;

	
}
