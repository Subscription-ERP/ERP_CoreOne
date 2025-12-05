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
import com.rootcore.hr.vo.UserPayManageDetailVO;
import com.rootcore.hr.vo.UserPayManageVO;
import com.rootcore.hr.vo.UserVO;

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
	public ModelAndView payslipPreview(@RequestParam String userPayManagementCode) {

	    // 1. 데이터 조회
	    UserPayManageDetailVO userPayManage = payrollManageService.selectPayrollManageDetail(userPayManagementCode);
	    if (userPayManage == null) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, userPayManage + "급여내역을 찾을 수 없습니다.");
	    }

	    // 2. 귀속연월 포맷팅 (동적 제목에 사용할 문자열 생성)
	    String payPeriodRaw = userPayManage.getPayPeriod(); // 예: "2025-02"
	    String payPeriodFormatted = null;
	    
	    if (payPeriodRaw != null && payPeriodRaw.matches("\\d{4}-\\d{2}")) {
	        // "2025-02" -> "2025년 02월"
	        payPeriodFormatted = payPeriodRaw.replace("-", "년 ") + "월";
	    }

	    // 3. 템플릿에 넘길 **data 맵 생성 (필수)**
	    Map<String, Object> data = new HashMap<>();
	    
	    // (a) 기존 객체 유지
	    data.put("userPayManage", userPayManage); 
	    // (b) 새로 만든 포맷된 문자열을 data 맵 안에 추가
	    data.put("payPeriodFormatted", payPeriodFormatted); 

	    // 4. ModelAndView 설정
	    ModelAndView mav = new ModelAndView("pdfView");

	    // PdfView가 요구하는 2가지 필수 키: 'templateName'과 'data'
	    mav.addObject("templateName", "pdf/payslip");
	    mav.addObject("data", data); // <--- data 맵 객체 전달 (PdfView의 핵심 요구사항)
	    mav.addObject("disposition", "inline");

	    return mav;
	}
	
	// 사원카드PDF 다운로드
	@GetMapping("/payslip/download")
	public ModelAndView payslipPDF(@RequestParam String userPayManagementCode) {
		
		// 1. 데이터 조회
	    UserPayManageDetailVO userPayManage = payrollManageService.selectPayrollManageDetail(userPayManagementCode);
	    if (userPayManage == null) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, userPayManage + "급여내역을 찾을 수 없습니다.");
	    }

	    // 2. 귀속연월 포맷팅 (동적 제목에 사용할 문자열 생성)
	    String payPeriodRaw = userPayManage.getPayPeriod(); // 예: "2025-02"
	    String payPeriodFormatted = null;
	    
	    if (payPeriodRaw != null && payPeriodRaw.matches("\\d{4}-\\d{2}")) {
	        // "2025-02" -> "2025년 02월"
	        payPeriodFormatted = payPeriodRaw.replace("-", "년 ") + "월";
	    }

	    // 3. 템플릿에 넘길 **data 맵 생성 (필수)**
	    Map<String, Object> data = new HashMap<>();
	    
	    // (a) 기존 객체 유지
	    data.put("userPayManage", userPayManage); 
	    // (b) 새로 만든 포맷된 문자열을 data 맵 안에 추가
	    data.put("payPeriodFormatted", payPeriodFormatted); 

	    // 4. ModelAndView 설정
	    ModelAndView mav = new ModelAndView("pdfView");

	    // PdfView가 요구하는 2가지 필수 키: 'templateName'과 'data'
	    mav.addObject("templateName", "pdf/payslip");
	    mav.addObject("data", data); // <--- data 맵 객체 전달 (PdfView의 핵심 요구사항)
	    mav.addObject("disposition", "payslip-" + userPayManage.getUserPayManagementCode() + ".pdf");

	    return mav;
	}
}
