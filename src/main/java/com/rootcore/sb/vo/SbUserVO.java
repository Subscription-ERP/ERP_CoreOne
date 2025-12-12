package com.rootcore.sb.vo;

import java.util.Date;

import lombok.Data;

@Data
public class SbUserVO {
	private String companyCode;
	private String userId;
	private String userName;
	private int salary;
	private String dept;
	private String jobTitle;
	private String position;
	private String roleCode;
	private String createdBy; // CREATED_BY
	private Date createDate; // CREATE_DATE
	private String updatedBy; // UPDATED_BY
	private Date updateDate; // UPDATE_DATE
}
