package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

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






}
