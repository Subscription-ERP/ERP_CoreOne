package com.rootcore.hr.vo;

import lombok.Data;

@Data
public class ReviewItemScoreVO {

	private Long itemNo;         // 항목 번호(23, 24 등)
    private String itemName;     // 항목명 (업무 성과, 협업 및 커뮤니케이션 등)
    private String description;  // 설명
    private int weight;          // 가중치(%)
    private String grade;        // 점수입력 (A~E)
	
}
