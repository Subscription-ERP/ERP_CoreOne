package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.hr.mapper.HrReviewMapper;
import com.rootcore.hr.service.HrReviewService;
import com.rootcore.hr.vo.EvalItemVO;
import com.rootcore.hr.vo.HrReviewMasterVO;

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

	// 인사평가관리 - 다건조회
	@Override
	public List<HrReviewMasterVO> selectReviewOnlyY() {
		return hrReviewMapper.selectReviewOnlyY();
	}







}
