package com.rootcore.hr.mapper;

import java.util.List;

import com.rootcore.hr.vo.EvalItemVO;
import com.rootcore.hr.vo.HrReviewMasterVO;

public interface HrReviewMapper {
	
	// 인사평가
	List<HrReviewMasterVO> selectHrReviewMasterALL();             // 인사평가 기준관리 - 다건조회
	HrReviewMasterVO selectHrReviewMaster(String rmCode);         // 인사평가 기준관리 - 단건조회
	List<EvalItemVO> selectEvalItem(String rmCode);               // 인사평가 기준관리 - 평가항목 조회
	List<HrReviewMasterVO> searchHrReview(HrReviewMasterVO hrReviewMasterVO);   // 인사평가 기준관리 - 검색
}
