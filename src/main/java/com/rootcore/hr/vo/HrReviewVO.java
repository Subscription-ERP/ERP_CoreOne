package com.rootcore.hr.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class HrReviewVO {

	// 인사평가관리
	private String companyCode;            //회사코드_FK
	private String reviewMasterCode;       //인사평가기준코드_FK
	private String reviewCode;             //인사평가번호
	private String raterUserId;            //평가자_FK
	private String targetUserId;           //피평가자_FK
	private String reviewStatus;           //평가상태
	private String finalScore;             //최종점수
	private String reviewComment;          //평가코멘트
	private String createdBy;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	private String updatedBy;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updateDate;
	
	private String targetUserName;          // 피평가자 이름
	private String targetDept;              // 피평가자 부서
	private String targetDeptName;          // 피평가자 부서명
	private String targetJobTitle;          // 피평가자 직급/직위
	private String targetJobTitleName;      // 피평가자 직급/직위명
	
	private List<HrReviewResultVO> hrReviewResultList;     // 평가결과 리스트
	
}
