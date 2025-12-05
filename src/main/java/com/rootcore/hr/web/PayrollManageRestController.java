package com.rootcore.hr.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.rootcore.hr.service.PayrollManageService;
import com.rootcore.hr.vo.PayrollManageSearchVO;
import com.rootcore.hr.vo.UserPayManageVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class PayrollManageRestController {
	private final PayrollManageService payrollManageService;

	// 급여관리-사원급여조회
	@GetMapping("/payrollManageList")
	public Map<String, Object> getPayrollManageList(PayrollManageSearchVO param) {
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

	// 사원카드PDF 미리보기
	@GetMapping("/payslip/preview")
	public ModelAndView userCardPreview(@RequestParam String userPayManagementCode) {

		// 데이터조회
		UserPayManageVO userPayManage = payrollManageService.selectPayrollManageDetail(userPayManagementCode);
		if (userPayManage == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, userPayManage + "급여내역을 찾을 수 없습니다.");
		}

		// 템플릿에 넘길 data 맵 생성
		Map<String, Object> data = new HashMap<>();
		data.put("userPayManage", userPayManage);

		// pdfView로 ModelAndView 생성
		ModelAndView mav = new ModelAndView("pdfView");

		// pdfView에서 사용할 템플릿 이름(template/pdf/payslip.html)
		mav.addObject("templateName", "pdf/payslip");

		// 템플릿에 전달할 실제 데이터
		mav.addObject("data", data);

		// 미리보기
		mav.addObject("disposition", "inline");

		return mav;

	}
}
