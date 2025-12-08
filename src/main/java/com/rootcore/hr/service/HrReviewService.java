package com.rootcore.hr.service;

import java.util.List;

import com.rootcore.hr.vo.EvalItemVO;
import com.rootcore.hr.vo.HrReviewMasterVO;

public interface HrReviewService {

	// 인사평가 
	List<HrReviewMasterVO> selectHrReviewMasterALL();                   // 인사평가 기준관리 - 전체조회
	HrReviewMasterVO selectHrReviewDetail(String rmCode);               // 인사평가 기준관리 - 단건조회 + 평가항목
	List<HrReviewMasterVO> searchHrReview(HrReviewMasterVO hrReviewMasterVO);   // 인사평가 기준관리 - 검색
	int registerReview(HrReviewMasterVO hrReviewMasterVO);              // 인사평가 기준관리 - 등록
	int modifyReview(HrReviewMasterVO hrReviewMasterVO);                // 인사평가 기준관리 - 수정
	
	List<HrReviewMasterVO> selectReviewOnlyY();                         // 인사평가관리 - 다건조회
}
