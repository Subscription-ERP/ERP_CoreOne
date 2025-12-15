package com.rootcore.hr.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.PayrollService;
import com.rootcore.hr.vo.PayrollUserVO;
import com.rootcore.hr.vo.PayrollVO;
import com.rootcore.hr.vo.UserPayManageVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class PayrollRestController {
	private final PayrollService payrollService;

	// 급여대장-상여등록
	@PostMapping("/bonusRegister")
	public Map<String, Object> insertBonusPayroll(@RequestBody PayrollVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		param.setCompanyCode(CompanyCode);

		int count = payrollService.insertBonusPayroll(param);

		Map<String, Object> result = new HashMap<>();
		if (count > 0) {
			result.put("success", true);
			result.put("count", count);
			result.put("message", "등록 성공");
		} else {
			result.put("success", false);
			result.put("message", "등록된 데이터가 없습니다.");
		}
		return result;
	}

	// 급여대장-상여등록-사원조회
	@GetMapping("/payrollEmpList")
	public Map<String, Object> getUserList(PayrollUserVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		param.setCompanyCode(CompanyCode);

		// 사원조회
		List<PayrollUserVO> list = payrollService.selectUserList(param);

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

	// 급여대장-급여대장목록조회
	@GetMapping("/payrollList")
	public Map<String, Object> getPayrollList(PayrollVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		param.setCompanyCode(CompanyCode);

		// 급여대장조회
		List<PayrollVO> list = payrollService.selectPayrollList(param);
		System.out.println("list>>>" + list);
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

	// 계산하기 (급여 기간 코드를 받아 Service에서 요구하는 Map 형태로 변환 후 호출)
	@GetMapping("/UserPayList")
	public Map<String, Object> getUserPayList(@RequestParam("payroll_period_code") String payrollPeriodCode) {

		// 1. Service/Mapper가 Map을 요구하므로 Map을 생성합니다.
		Map<String, Object> paramMap = new HashMap<>();

		// 2. Map에 급여 기간 코드를 저장합니다.
		// *주의: ServiceImpl 및 Mapper XML에서 "payrollPeriodCode" (카멜 케이스) 키를 사용하므로
		// 여기에 맞춰서 Map에 값을 담아야 합니다.
		paramMap.put("payrollPeriodCode", payrollPeriodCode);

		// 3. Service 호출: Map을 전달하여 SP 실행 및 결과를 Map에 받아옵니다.
		List<UserPayManageVO> payList = payrollService.selectUserPayManageList(paramMap);
		System.out.println("payList:" + payList);

		// 4. API 응답 형식에 맞춰 결과를 반환합니다.
		Map<String, Object> result = new HashMap<>();

		if (payList != null && !payList.isEmpty()) {
			result.put("result", true);
			result.put("data", Map.of("contents", payList)); // Map.of를 사용하여 데이터 구조화
			result.put("message", "급여 계산 완료");
		} else {
			result.put("result", false);
			result.put("message", "계산된 급여 데이터가 없습니다.");
		}

		return result;
	}

	// 급여대장-계산하기모달-확정버튼
	@PostMapping("/registerUserPay")
	public int registerUserPay(@RequestBody List<UserPayManageVO> userPayManageList) {
		return payrollService.insertUserPay(userPayManageList);
	}

	// 급여대장-계산하기-급여관리모달창-확정버튼-이미 있는 데이터인지 확인하는 select
	@GetMapping("/checkUserPay")
	public int checkUserPay(@RequestParam("payroll_code") String payrollCode) {
		return payrollService.checkUserPay(payrollCode);
	}

}
