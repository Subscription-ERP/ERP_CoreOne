package com.rootcore.sb.vo;

import java.util.Date;

import lombok.Data;

@Data
public class SbLoginVO {
	private String companyCode;
	private String userId;
	private String passWord;
	private String userName;
	private int failCount;
	private String status;
	
	private String createdBy; // CREATED_BY
	private Date createDate; // CREATE_DATE
	private String updatedBy; // UPDATED_BY
	private Date updateDate; // UPDATE_DATE
	
	
}
