package com.rootcore.hr.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserVO {	
	
	private String companyCode;           // 회사코드
	private String userId;                // 사원번호
	private String userName;              // 사원명
	private String tel;
	private String email;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date hireDate;                // 입사일
	private String hireType;              // 인사구분
	private String hireTypeName;          // 입사구분명
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date leaveDate;               // 퇴사일
	private String leaveReason;           // 퇴사사유
	private String zipCode;               // 우편번호
	private String address;               // 주소
	private String dept;                  // 부서
	private String deptName;              // 부서명
	private String jobTitle;              // 직위,직급
	private String jobTitleName;          // 직위,직급명
	private String position;              // 직책
	private String positionName;          // 직책명
	private Long familyCount;             // 가족수
	private Long childrenCount;           // 자녀수
	private String householder;           // 세대주여부
	private String bankName;              // 은행
	private String accountNo;             // 계좌번호
	private String accountHolder;         // 예금주
	private Long salary;                  // 급여
	private String userStatus;            // 재직상태
	private String userStatusName;        // 재직상태명
	private String userPhoto;             // 사원사진
	private String userFile;              // 사원파일
	private String remark;                // 비고
	private String createdBy; 
	private Date createDate;
	private String updatedBy;
	private Date updateDate;
	
	private List<CertificationVO> certificationList;          // 자격증
	private List<WorkExperienceVO> workExperienceList;        // 경력사항
	private List<UserHistoryVO> historyList;                  // 이력
	
}
