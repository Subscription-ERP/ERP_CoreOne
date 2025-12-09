package com.rootcore.hr.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rootcore.hr.vo.EvalItemVO;
import com.rootcore.hr.vo.HrReviewMasterVO;
import com.rootcore.hr.vo.HrReviewVO;

public interface HrReviewService {

	// 인사평가 
	List<HrReviewMasterVO> selectHrReviewMasterALL();                   // 인사평가 기준관리 - 전체조회
	HrReviewMasterVO selectHrReviewDetail(String rmCode);               // 인사평가 기준관리 - 단건조회 + 평가항목
	List<HrReviewMasterVO> searchHrReview(HrReviewMasterVO hrReviewMasterVO);   // 인사평가 기준관리 - 검색
	int registerReview(HrReviewMasterVO hrReviewMasterVO);              // 인사평가 기준관리 - 등록
	int modifyReview(HrReviewMasterVO hrReviewMasterVO);                // 인사평가 기준관리 - 수정
	
	List<HrReviewMasterVO> selectReviewOnlyY();                         // 인사평가관리 - 기준관리 다건조회
	List<EvalItemVO> selectEvalItemByMcode(String reviewMasterCode);    // 인사평가관리 - 기준관리 평가항목
	
	// 인사평가관리 - 팀원수,review갯수 확인(부서팀장별)
	List<HrReviewMasterVO> selectCheckReviewCount(@Param("companyCode") String companyCode,
                                                  @Param("userId") String userId);  
	
	// 인사평가관리 - 팀원 목록
	List<HrReviewVO> selectTeamList(@Param("companyCode") String companyCode,
                                    @Param("raterUserId") String raterUserId,
                                    @Param("reviewMasterCode") String reviewMasterCode); 
	
	// 인사평가관리 - 검색
	List<HrReviewMasterVO> searchHrReviewManage(String reviewMasterName);
	
	
	
	
}
