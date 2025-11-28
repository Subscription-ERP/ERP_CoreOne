package com.rootcore.sb.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PlanVO {
	private String planCode;
    private String planName;
    private String planInfo;
    private BigDecimal price;
    private Integer subsPeriod;
    private Integer userCount;

}
