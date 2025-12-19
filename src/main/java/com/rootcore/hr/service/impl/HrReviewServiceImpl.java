package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.hr.mapper.HrReviewMapper;
import com.rootcore.hr.service.HrReviewService;
import com.rootcore.hr.vo.EvalItemVO;
import com.rootcore.hr.vo.HrReviewMasterVO;
import com.rootcore.hr.vo.HrReviewResultVO;
import com.rootcore.hr.vo.HrReviewVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HrReviewServiceImpl implements HrReviewService {

	final private HrReviewMapper hrReviewMapper;
	
	// 인사평가 기준관리 - 다건조회
	@Override
	public List<HrReviewMasterVO> selectHrReviewMasterALL() {
		return hrReviewMapper.selectHrReviewMasterALL();
	}

	// 인사평가 기준관리 - 상세조회(단건조회 + 평가항목)
	@Override
	public HrReviewMasterVO selectHrReviewDetail(String rmCode) {
		// 단건조회
		HrReviewMasterVO hrReviewMasterVO = hrReviewMapper.selectHrReviewMaster(rmCode);
		
		// 없을 경우 
		if(hrReviewMasterVO == null) {
			return null;
		}
		
		// 평가사항 조회
		List<EvalItemVO> evalItem = hrReviewMapper.selectEvalItem(rmCode);
		
		// hrReviewMasterVO안에 세팅
		hrReviewMasterVO.setEvalItemList(evalItem);
				
		return hrReviewMasterVO;
	}

	// 인사평가 기준관리 - 검색
	@Override
	public List<HrReviewMasterVO> searchHrReview(HrReviewMasterVO hrReviewMasterVO) {
		return hrReviewMapper.searchHrReview(hrReviewMasterVO);
	}

	// 인사평가 기준관리 - 등록
	@Transactional
	@Override
	public int registerReview(HrReviewMasterVO hrReviewMasterVO) {
		
		int reviewResult = hrReviewMapper.insertReview(hrReviewMasterVO);
		if(reviewResult == 0) return 0;
		
		String companyCode = hrReviewMasterVO.getCompanyCode();
		String reviewMasterCode = hrReviewMasterVO.getReviewMasterCode();
		
		if(hrReviewMasterVO.getEvalItemList() != null && !hrReviewMasterVO.getEvalItemList().isEmpty()) {
			for(EvalItemVO eval : hrReviewMasterVO.getEvalItemList()) {
				eval.setCompanyCode(companyCode);
				eval.setReviewMasterCode(reviewMasterCode);
				int evalResult = hrReviewMapper.insertEvalItem(eval);
				if(evalResult == 0) return 0;
			}
		}		
		return 1;
	}

	// 인사평가 기준관리 - 수정
	@Transactional
	@Override
	public int modifyReview(HrReviewMasterVO hrReviewMasterVO) {
		
		int reviewResult = hrReviewMapper.updateReview(hrReviewMasterVO);
		if(reviewResult == 0) return 0;
		
		String companyCode = hrReviewMasterVO.getCompanyCode();
		String reviewMasterCode = hrReviewMasterVO.getReviewMasterCode();
		
        hrReviewMapper.deleteEvalItem(reviewMasterCode, companyCode); // 기존 평가항목 삭제
		
		if(hrReviewMasterVO.getEvalItemList() != null) {
			for(EvalItemVO eval : hrReviewMasterVO.getEvalItemList()) {
				eval.setCompanyCode(companyCode);
				eval.setReviewMasterCode(reviewMasterCode);
				int evalResult = hrReviewMapper.insertEvalItem(eval);
				if(evalResult == 0) return 0;
			}
		}
		return 1;
	}

	// 인사평가관리 - 기준관리 다건조회
	@Override
	public List<HrReviewMasterVO> selectReviewOnlyY() {
		return hrReviewMapper.selectReviewOnlyY();
	}
	
	// 인사평가관리 - 기준관리 평가항목
	@Override
	public List<EvalItemVO> selectEvalItemByMcode(String reviewMasterCode) {
		return hrReviewMapper.selectEvalItemByMcode(reviewMasterCode);
	}
		
	// 인사평가관리 - 다건조회 + 부서 팀장별 팀원수 계산, 리뷰작성 상태
	@Override
	public List<HrReviewMasterVO> selectCheckReviewCount(String companyCode, String userId) {
		
		int totalMemberCnt = hrReviewMapper.selectTeamCount(companyCode, userId);  // 전체 팀원수
		List<HrReviewMasterVO> list = hrReviewMapper.selectReviewCount(companyCode, userId); // 리뷰작성
		
		for(HrReviewMasterVO hrReviewMasterVO : list) {
			hrReviewMasterVO.setTotalMemberCnt(totalMemberCnt);
			
			if(hrReviewMasterVO.getCompletedCnt() == 0) {
				hrReviewMasterVO.setReivewStatus("미평가");
			} else if(hrReviewMasterVO.getCompletedCnt() < totalMemberCnt) {
				hrReviewMasterVO.setReivewStatus("진행중");
			} else {
				hrReviewMasterVO.setReivewStatus("완료");
			}
		}
		
		return list;
	}

	
	// 인사평가관리 - 팀원 목록
	@Override
	public List<HrReviewVO> selectTeamList(String companyCode, String raterUserId, String reviewMasterCode) {
		return hrReviewMapper.selectTeamList(companyCode, raterUserId, reviewMasterCode);
	}

	// 인사평가관리 - 검색
	@Override
	public List<HrReviewMasterVO> searchHrReviewManage(String companyCode, 
                                                       String userId, 
                                                       String reviewMasterName) {
		
		// 기존 상태 계산 로직 재사용
		List<HrReviewMasterVO> list = selectCheckReviewCount(companyCode, userId);
		
		// reviewMasterName값이 없을 경우
		if(reviewMasterName == null || reviewMasterName.isBlank()) {
			return list;
		}
		
		// reviewMasterName(검색어)가 있을 경우
		return list.stream()
				   .filter(vo -> vo.getReviewMasterName() != null && vo.getReviewMasterName().contains(reviewMasterName))
				   .toList();
	
		
	}

	// 인사평가관리 - 등록
	@Transactional
	@Override
	public int registerReviewResult(HrReviewVO hrReviewVO) {
		
		int review = hrReviewMapper.insertReviewItem(hrReviewVO);
		if(review == 0) return 0;
		
		String reviewCode = hrReviewVO.getReviewCode();
		
		if(hrReviewVO.getHrReviewResultList() != null && !hrReviewVO.getHrReviewResultList().isEmpty()) {
			for(HrReviewResultVO reviewResult : hrReviewVO.getHrReviewResultList()) {
				reviewResult.setReviewCode(reviewCode);
				reviewResult.setCreatedBy(hrReviewVO.getCreatedBy());
				
				int evalResult = hrReviewMapper.insertReviewResult(reviewResult);
				if(evalResult == 0) return 0;
			}
		}		
		return 1;

	}

	
	
	// 인사평가관리 - 상세조회
	@Override
	public HrReviewVO reviewResultByTargetUserId(String targetUserId, String reviewCode) {

		// 상위 조회
		HrReviewVO hrReviewVO = hrReviewMapper.selectReviewResultHeaderByTargetUserId(targetUserId, reviewCode);
		
		// 하위 조회
		List<HrReviewResultVO> reviewResult = hrReviewMapper.selectReviewResultByTargetUserId(reviewCode);
		
		// 값 담기
		hrReviewVO.setHrReviewResultList(reviewResult);
		
		return hrReviewVO;
	}

	
	// 인사평가관리 - 수정
	@Transactional
	@Override
	public int modifyReviewResult(HrReviewVO hrReviewVO) {
		
		// 기본사항
		int review = hrReviewMapper.updateReviewResultHeader(hrReviewVO);
		if(review == 0) return 0;
		
		String reviewCode = hrReviewVO.getReviewCode();
		
		// 평가항목 결과
		if(hrReviewVO.getHrReviewResultList() != null && !hrReviewVO.getHrReviewResultList().isEmpty()) {
			for(HrReviewResultVO reviewResult : hrReviewVO.getHrReviewResultList()) {
				reviewResult.setReviewCode(reviewCode);
				reviewResult.setUpdatedBy(hrReviewVO.getUpdatedBy());
				
				int result = hrReviewMapper.updateReviewResult(reviewResult);
				if(result == 0) return 0;
			}
		}
		
		return 1;
	}




}
