package com.rootcore.hr.web;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HrController {
	
	// 공통
	@GetMapping("/hr")
	public String hrMain() {
		return "hr/main";
	}
	
	// 사원관리
	@GetMapping("/hr/userManage")
	public String userManage(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "인사",
		        "사원관리"
		    ));
		return "hr/userManage";
	}
	// 조직도 관리
	@GetMapping("/hr/deptManage")
	public String deptManage(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "인사",
		        "조직도 관리"
		    ));
		return "hr/deptManage";
	}
	
	// 연차
	// 연차관리
	@GetMapping("/hr/annualManage")
	public String annaulManage(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "인사",
		        "연차관리"
		    ));
		return "hr/annual/annualManage";
	}
	// 연차현황
	@GetMapping("/hr/annualStatus")
	public String annualStatus(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "인사",
		        "연차현황조회"
		    ));
		return "hr/annual/annualStatus";
	}
	
	// 근태
	// 근태관리(관리자)
	@GetMapping("/hr/attendanceManage")
	public String attendanceManage(Model model) {
		
		model.addAttribute("breadcrumb", List.of(
		        "인사",
		        "근태관리"
		    )); 
		
		List<Map<String, String>> attendTypes = List.of(
		        Map.of("type", "h1", "label", "정상", "color", "#A0A0A0"),
		        Map.of("type", "h2", "label", "지각", "color", "#FB8C00"),
		        Map.of("type", "h3", "label", "조퇴", "color", "#FFB74D"),
		        Map.of("type", "h4", "label", "결근", "color", "#E53935"),
		        Map.of("type", "h5", "label", "연차", "color", "#4FC3F7"),
		        Map.of("type", "h6", "label", "반차", "color", "#AED581"),
		        Map.of("type", "h7", "label", "병가", "color", "#9575CD"),
		        Map.of("type", "h10", "label", "휴무", "color", "#90A4AE")
		    );

		    model.addAttribute("attendTypes", attendTypes);
		
		return "hr/attendance/attendanceManage";
	}
	// 내근태관리
	@GetMapping("/hr/attendanceMe")
	public String attendanceMe(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "인사",
		        "내 근태관리"
		    ));
		return "hr/attendance/attendanceMe";
	}
	
	// 급여	
	// 급여 관리(조회) 페이지 이동
	@GetMapping("/hr/payrollManage")
	public String payrollManage(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "인사",
		        "급여조회"
		    ));
		return "hr/payroll/payrollManage";
	}
	// 급여 대장 페이지 이동
	@GetMapping("/hr/payrollReport")
	public String payrollReport(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "인사",
		        "급여대장"
		    ));
		return "hr/payroll/payrollReport";
	}
	
	// 인사평가	
	@GetMapping("/hr/reviewMaster")
	public String reviewMaster(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "인사",
		        "인사평가 기준관리"
		    ));
		return "hr/review/reviewMaster";
	}
	@GetMapping("/hr/reviewManage")
	public String reviewManage(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "인사",
		        "인사평가관리"
		    ));
		return "hr/review/reviewManage";
	}
	
	// 증명서 
	@GetMapping("/hr/certificationManage")
	public String certificationManage(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "인사",
		        "증명서관리"
		    ));
		return "hr/certificationManage";
	}
	
	
	
}
