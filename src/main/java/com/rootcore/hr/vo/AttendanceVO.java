package com.rootcore.hr.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AttendanceVO {
	
	private String companyCode;    //회사코드
	private String attenCode;      //근태코드
	private String userId;         //사원번호_FK
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date workDate;         //근무일자
	private String attendType;     //근태상태
	private String attendTypeName; //근태상태명
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Date inTime;           //출근시간
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Date outTime;          //퇴근시간
	private Double overWorkTime;     //연장근무시간
	private Double nightWorkTime;    //야간근무시간
	private Double holidayWorkTime;  //휴일근무시간
	private Double totalWorkTime;    //일일총근무시간
	private String remark;
	private String createdBy;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date createDate;
	private String updatedBy;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date updateDate;
	
	private String workPlaceType;   //근무형태

}
