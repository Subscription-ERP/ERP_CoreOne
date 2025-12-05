package com.rootcore.hr.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class EvalItemVO {

	// 인사평가 기준관리 - 평가항목
	
	private long evalSeq;              //평가항목번호
	private String companyCode;        //회사코드
	private String reviewMasterCode;  //인사평가기준코드
	private String evalName;           //항목명
	private String evalDetail;         //설명
	private long evalWeight;           //가중치(%)
	private String createdBy;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date createDate;
	private String updatedBy;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date updateDate;
	
}
