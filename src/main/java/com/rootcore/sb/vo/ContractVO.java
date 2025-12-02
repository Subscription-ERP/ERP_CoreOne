package com.rootcore.sb.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

@Data
public class ContractVO {
	
	private String contractCode;
    private String companyCode;
    private Date contractDate;

    private String companyName;
    private String ceoName;
    private String signName;
    private String filePath;

    private String planCode;

    private Date contractStart;
    private Date contractEnd;

    private Integer subsPeriod;
    private Integer userCount;

    private BigDecimal totalPrice;
    private BigDecimal vat;
    private BigDecimal discountAmount;

    private String status;

    private String createdBy;
    private LocalDateTime createDate;
    private String updatedBy;
    private LocalDateTime updateDate;

	
}
