package com.rootcore.hr.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	// 인사평가 기준관리 - 등록
	@PostMapping("/registerMaster")
	public ResponseEntity<Map<String, Object>> registerReviewMaster(@RequestBody HrReviewMasterVO hrReviewMasterVO) {

	    hrReviewMasterVO.setCompanyCode("0000");

	    int result = hrReviewService.registerReview(hrReviewMasterVO);

	    if (result == 1) {
	        Map<String, Object> body = new HashMap<>();
	        body.put("status", "success");
	        body.put("reviewMasterCode", hrReviewMasterVO.getReviewMasterCode());

	        return ResponseEntity.ok(body);
	    } else {
	        Map<String, Object> body = new HashMap<>();
	        body.put("status", "fail");
	        body.put("message", "인사평가 기준 등록 중 오류가 발생했습니다.");

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	    }
	}
	
	// 인사평가 기준관리 - 수정
	@PostMapping("/modifyMaster")
	public ResponseEntity<Map<String, Object>> modifyReviewMaster(@RequestBody HrReviewMasterVO hrReviewMasterVO){
		
		hrReviewMasterVO.setCompanyCode("0000");
		
		int result = hrReviewService.modifyReview(hrReviewMasterVO);
		
		if(result == 1) {
			Map<String, Object> body = new HashMap<>();
	        body.put("status", "success");
	        body.put("reviewMasterCode", hrReviewMasterVO.getReviewMasterCode());
	        
	        return ResponseEntity.ok(body);
		}else {
			Map<String, Object> body = new HashMap<>();
	        body.put("status", "fail");
	        body.put("message", "인사평가 기준 수정 중 오류가 발생했습니다.");
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
		}
	
	}
	
	
	// 인사평가 관리 - 전체조회('Y'만)
	@GetMapping("/manage")
	public List<HrReviewMasterVO> reviewManageAllList(){
		return hrReviewService.selectReviewOnlyY();
	}
	
	
	
	
}
