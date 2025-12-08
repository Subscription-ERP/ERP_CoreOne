package com.rootcore.hr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rootcore.hr.vo.EvalItemVO;
import com.rootcore.hr.vo.HrReviewMasterVO;

public interface HrReviewMapper {
	
	// 인사평가
	List<HrReviewMasterVO> selectHrReviewMasterALL();             // 인사평가 기준관리 - 다건조회
	HrReviewMasterVO selectHrReviewMaster(String rmCode);         // 인사평가 기준관리 - 단건조회
	List<EvalItemVO> selectEvalItem(String rmCode);               // 인사평가 기준관리 - 평가항목 조회
	List<HrReviewMasterVO> searchHrReview(HrReviewMasterVO hrReviewMasterVO);   // 인사평가 기준관리 - 검색
	int insertReview(HrReviewMasterVO hrReviewMasterVO);          // 인사평가 기준관리 - 등록(기본정보)
	int insertEvalItem(EvalItemVO evalItemVO);                    // 인사평가 기준관리 - 등록(평가항목)
	int updateReview(HrReviewMasterVO hrReviewMasterVO);          // 인사평가 기준관리 - 수정(기본정보)
	int deleteEvalItem(@Param("reviewMasterCode") String reviewMasterCode, 
			           @Param("companyCode") String companyCode); // 인사평가 기준관리 - 삭제(평가항목)
	
	List<HrReviewMasterVO> selectReviewOnlyY();                   // 인사평가관리 - 다건조회
	
	
}
