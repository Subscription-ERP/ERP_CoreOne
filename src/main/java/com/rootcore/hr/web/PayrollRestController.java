package com.rootcore.hr.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.PayrollService;
import com.rootcore.hr.vo.PayrollVO;
import com.rootcore.hr.vo.UserVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class PayrollRestController {
	private final PayrollService payrollService;

	// 급여대장-상여등록-사원조회
	@GetMapping("/payrollEmpList")
	public Map<String, Object> getUserList(UserVO param) {
		// 사원조회
		List<UserVO> list = payrollService.selectUserList(param);

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

	// 급여대장-조회
	@GetMapping("/payrollList")
	public Map<String, Object> getPayrollList(PayrollVO param) {
		// 급여대장조회
		List<PayrollVO> list = payrollService.selectPayrollList(param);
		
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
