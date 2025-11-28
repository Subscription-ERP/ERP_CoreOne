package com.rootcore.hr.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DeptVO {

	private String deptCode;                     // 부서코드
	private String companyCode;                  // 회사코드
	private String deptName;                     // 부서명
	private String upperDeptNo;                 // 상위부서번호
	private Long deptLevel;                      // 부서레벨
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;                      // 적용시작일
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;                        // 적용종료일
	private String status;                        // 상태
	private String deptMng;                      // 부서관리자
	private String rm;                            // 비고
	private String createdBy;                    // 생성자
	private Date createDate;                     // 생성일자
	private String updatedBy;                    // 수정자
	private Date updateDate;                     // 수정일자
	
}
