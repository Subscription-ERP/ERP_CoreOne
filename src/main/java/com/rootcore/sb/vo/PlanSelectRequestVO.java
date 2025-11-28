package com.rootcore.sb.vo;

import lombok.Data;

@Data
public class PlanSelectRequestVO {
	private String companyCode;
    private String planCode;

    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }

    public String getPlanCode() { return planCode; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }
}
