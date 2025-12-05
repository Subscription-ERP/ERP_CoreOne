package com.rootcore.hr.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.HrReviewService;
import com.rootcore.hr.vo.HrReviewMasterVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class HrReviewRestController {

	final private HrReviewService hrReviewService;
	
	// 인사평가 기준관리 - 다건조회
	@GetMapping("")
	public List<HrReviewMasterVO> reviewAllList(){
		return hrReviewService.selectHrReviewMasterALL();
	}
	
	// 인사평가 기준관리 - 상세조회(단건조회 + 평가항목)
	@GetMapping("/detail")
	public HrReviewMasterVO reviewDetail(String rmCode) {
		return hrReviewService.selectHrReviewDetail(rmCode);
	}
	
	// 인사평가 기준관리 - 검색
	@GetMapping("/search")
	public List<HrReviewMasterVO> searchReviewList(HrReviewMasterVO hrReviewMasterVO,
			@RequestParam(value = "useYnList", required = false) List<String> useYnList) {
		
		hrReviewMasterVO.setUseYnList(useYnList);
		return hrReviewService.searchHrReview(hrReviewMasterVO);

	}
	
	
}
