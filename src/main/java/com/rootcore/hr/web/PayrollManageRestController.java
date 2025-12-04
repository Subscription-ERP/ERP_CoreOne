package com.rootcore.hr.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.PayrollManageService;
import com.rootcore.hr.vo.UserPayManageVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class PayrollManageRestController {
	private final PayrollManageService payrollManageService;

	// 급여관리-사원급여조회
	@GetMapping("/payrollManageList")
	public Map<String, Object> getPayrollManageList(UserPayManageVO param) {
		// 사원급여조회
		List<UserPayManageVO> list = payrollManageService.selectPayrollManageList(param);

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
}
