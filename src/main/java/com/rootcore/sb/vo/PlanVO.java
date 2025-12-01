package com.rootcore.sb.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class PlanVO {
	private String planCode;     // PLAN_CODE
    private String planName;     // PLAN_NAME
    private String planInfo;     // PLAN_INFO
    private BigDecimal price;    // PRICE
    private Integer subsPeriod;  // SUBS_PERIOD
    private Integer userCount;   // USER_COUNT
    private String createdBy;    // CREATED_BY
    private Date createDate;     // CREATE_DATE
    private String updatedBy;    // UPDATED_BY
    private Date updateDate;     // UPDATE_DATE
    

}
