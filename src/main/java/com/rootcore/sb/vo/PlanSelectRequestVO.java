package com.rootcore.sb.vo;

import lombok.Data;

@Data
public class PlanSelectRequestVO {
	private String companyCode;
    private String planCode;
    private Integer subsPeriod;
    private Integer userCount;

}
