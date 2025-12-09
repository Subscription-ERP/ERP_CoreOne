package com.rootcore.hr.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class HrReviewResultVO {

	// 인사평가관리 평가항목별 결과	
	private long reviewResultSeq;     //평가항목결과번호
	private String reviewCode;        //인사평가코드
	private String evalItemScore;     //평가항목별점수
	private String createdBy;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	private String updatedBy;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updateDate;
	
}
