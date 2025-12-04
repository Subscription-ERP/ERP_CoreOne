package com.rootcore.hr.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AttendanceVO {
	
	String companyCode;    //회사코드
	String attenCode;      //근태코드
	String userId;         //사원번호_FK
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	Date workDate;         //근무일자
	String attendType;     //근태상태
	String attendTypeName; //근태상태명
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	Date inTime;           //출근시간
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	Date outTime;          //퇴근시간
	Long overWorkTime;     //연장근무시간
	Long nightWorkTime;    //야간근무시간
	Long holidayWorkTime;  //휴일근무시간
	Long totalWorkTime;    //일일총근무시간
	String remark;
	String createdBy;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	Date createDate;
	String updatedBy;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	Date updateDate;

}
