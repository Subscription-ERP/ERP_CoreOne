package com.rootcore.hr.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.AnnualStatusService;
import com.rootcore.hr.vo.AnnualLeaveVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/*
 * 연차현황조회 페이지 컨트롤러
 * AnnualStatusRestController
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class AnnualStatusRestController {
	private final AnnualStatusService annualStatusService;

	// 회사 사원들 연차 현황 조회
	@GetMapping("/annualStatusList")
	public Map<String, Object> annualStatusList(AnnualLeaveVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		param.setCompanyCode(CompanyCode);

		// 연차현황조회
		List<AnnualLeaveVO> list = annualStatusService.selectAnnualStatusList(param);

		// RESULT에 실행결과 저장
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);

		// data 내부에 사원조회한거 저장
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);
		
		// 그럼 RESULT에 실행결과, DATA 요렇게 저장되서 넘어감
		result.put("data", data);

		return result;
	}
}
