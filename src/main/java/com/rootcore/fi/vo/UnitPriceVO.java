package com.rootcore.fi.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UnitPriceVO {

	private String companyCode;
	private String sku;
	private String skuName;
	private String custCode;
	private String custName;
	private String unitPriceType;
	private String unitPriceTypeName;
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date startDate;
	private double unitPrice;
}
