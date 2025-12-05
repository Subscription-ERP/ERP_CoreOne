package com.rootcore.hr.vo;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class HrReviewMasterVO {

	// 인사평가 기준관리
	
	private String companyCode;        //회사코드
	private String reviewMasterCode;   //인사평가기준코드
	private String reviewMasterName;   //템플릿명
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date reviewStartDate;      //평가시작일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date reviewEndDate;        //평가종료일
	private String useYn;              //사용여부
	private String remark;
	private String createdBy;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date createDate;
	private String updatedBy;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date updateDate;
	
	private List<EvalItemVO> evalItemList;          // 평가항목
	private List<String> useYnList;                 // 사용여부 리스트(검색)
	
}
