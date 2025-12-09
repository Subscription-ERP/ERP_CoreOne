package com.rootcore.hr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rootcore.hr.vo.EvalItemVO;
import com.rootcore.hr.vo.HrReviewMasterVO;
import com.rootcore.hr.vo.HrReviewResultVO;
import com.rootcore.hr.vo.HrReviewVO;

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
	
	List<HrReviewMasterVO> selectReviewOnlyY();                         // 인사평가관리 - 기준관리 다건조회
	List<EvalItemVO> selectEvalItemByMcode(String reviewMasterCode);    // 인사평가관리 - 기준관리 평가항목                      
	
	// 인사평가관리 - 팀원수 계산
	int selectTeamCount(@Param("companyCode") String companyCode,
                        @Param("userId") String userId);                
	
	// 인사평가관리 - 작성,완성된 Review 갯수
	List<HrReviewMasterVO> selectReviewCount(@Param("companyCode") String companyCode,
			                                 @Param("userId") String userId);  
	
	// 인사평가관리 - 팀원 목록
	List<HrReviewVO> selectTeamList(@Param("companyCode") String companyCode,
                                    @Param("raterUserId") String raterUserId,
                                    @Param("reviewMasterCode") String reviewMasterCode); 
	
	int insertReviewItem(HrReviewVO hrReviewVO);                  // 인사평가관리 - 등록(기본정보) 
	int insertReviewResult(HrReviewResultVO hrReviewResultVO);    // 인사평가관리 - 등록(평가결과)
	
	
	
}
