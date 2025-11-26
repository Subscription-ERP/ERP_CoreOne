package com.rootcore.hr.vo;

import java.util.Date;

import lombok.Data;

// 급여대장-상여등록-사원조회VO
@Data
public class UserVO {	
	
	private String companyCode;      //회사코드
	private String userId;           //사원번호
	private String userName;         //사원명
	private Date birth; 
	private String tel;
	private String email;
	private Date hireDate;           //입사일
	private String hireType;         //입사구분
	private Date leaveDate;          //퇴사일
	private String leaveReason;      //퇴사사유
	private String zipCode;          //우편번호
	private String address;          //주소
	private String dept;             //부서
	private String jobTitle;         //직급,직위
	private String position;         //직책
	private Long familyCount;        //가족수
	private Long childrenCount;      //자녀수
	private String householder;      //세대주여부
	private String bankName;         //은행
	private String accountNo;        //계좌번호
	private String accountHolder;    //예금주
	private Long salary;             //급여
	private String userStatus;       //재직상태
	private String userPhoto;        //사원사진
	private String userFile;         //사원파일
	private String remark;           //비고
	private String createdBy; 
	private Date createDate;
	private String updatedBy;
	private Date updateDate;
	
	
}
