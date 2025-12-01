package com.rootcore.fi.vo;

import lombok.Data;

@Data
public class CreditVO {
	public String companyCode;
	public String custCode;
	public String custName;
	public String creditType;
	public String creditTypeName;
	public int creditMax;
	public int creditDueDay;
}
