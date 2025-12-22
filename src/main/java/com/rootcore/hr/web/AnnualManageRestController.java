package com.rootcore.hr.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.AnnualManageService;
import com.rootcore.hr.vo.AnnualLeaveDetailVO;
import com.rootcore.hr.vo.AnnualLeaveVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class AnnualManageRestController {
	private final AnnualManageService annualManageService;

	// 사원연차신청이력조회
	@GetMapping("/annualManageList")
	public Map<String, Object> annualManageList(AnnualLeaveDetailVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		String userId = (String) session.getAttribute("LOGIN_USER_ID");
		param.setCompanyCode(CompanyCode);
		param.setUserId(userId);

		// 사원급여조회
		List<AnnualLeaveDetailVO> list = annualManageService.selectAnnualManageList(param);

		// RESULT에 실행결과 저장
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);

		// data 내부에 사원조회한거 저장
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);

		// 그럼 RESULT에 실행결과, DATA(사원조회한거) 요렇게 저장되서 넘어감
		result.put("data", data);

		return result;
	}

	// 현재 내 연차 현황
	@GetMapping("/myAnnualStatus")
	public AnnualLeaveVO myAnnualStatus(AnnualLeaveVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		String userId = (String) session.getAttribute("LOGIN_USER_ID");
		param.setCompanyCode(CompanyCode);
		param.setUserId(userId);
		return annualManageService.selectmyAnnualStatus(param);
	}

	// 연차신청
	@PostMapping("/myAnnualApply")
	public int myAnnualApply(@RequestBody AnnualLeaveDetailVO param, HttpSession session) {
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		String userId = (String) session.getAttribute("LOGIN_USER_ID");
		param.setCompanyCode(CompanyCode);
		param.setUserId(userId);
		return annualManageService.insertmyAnnualApply(param);
	}
}
