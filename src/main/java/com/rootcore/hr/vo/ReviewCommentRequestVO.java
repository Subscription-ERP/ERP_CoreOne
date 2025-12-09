package com.rootcore.hr.vo;

import java.util.List;

import lombok.Data;

@Data
public class ReviewCommentRequestVO {

	private String userName;     // 피평가자 이름
    private String jobTitle;     // 직무/직책
    private String period;       // 평가기간 (예: "2025년 하반기")
    private List<ReviewItemScoreVO> items;
	
}
