package com.rootcore.fi.vo;

import java.util.Date;

import lombok.Data;

@Data
public class UnitPriceVO {

	private String companyCode;
	private String sku;
	private String skuName;
	private String custCode;
	private String custName;
	private String unitPirceType;
	private String unitPriceTypeName;
	private Date startDate;
	private double unitPrice;
}
