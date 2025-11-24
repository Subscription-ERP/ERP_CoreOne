package com.rootcore.hr.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HrController {
	
	// 공통
	@GetMapping("/hr")
	public String hrMain() {
		return "hr/main";
	}
	@GetMapping("/hr/userManage")
	public String userManage() {
		return "hr/userManage";
	}
	@GetMapping("/hr/deptManage")
	public String deptManage() {
		return "hr/deptManage";
	}
	
	// 연차
	@GetMapping("/hr/annualManage")
	public String annaulManage() {
		return "hr/annual/userManage";
	}
	@GetMapping("/hr/annualStatus")
	public String annualStatus() {
		return "hr/annual/annualStatus";
	}
	
	// 근태
	@GetMapping("/hr/attendanceManage")
	public String attendanceManage() {
		return "hr/attendance/attendanceManage";
	}
	@GetMapping("/hr/attendanceMe")
	public String attendanceMe() {
		return "hr/attendance/attendanceMe";
	}
	
	// 급여	
	@GetMapping("/hr/payrollManage")
	public String payrollManage() {
		return "hr/payroll/payrollManage";
	}
	@GetMapping("/hr/payrollReport")
	public String payrollReport() {
		return "hr/payroll/payrollReport";
	}
	
	// 인사평가	
	@GetMapping("/hr/reviewMaster")
	public String reviewMaster() {
		return "hr/review/reviewMaster";
	}
	@GetMapping("/hr/reviewManage")
	public String reviewManage() {
		return "hr/review/reviewManage";
	}
	
	// 증명서 
	@GetMapping("/hr/certificationManage")
	public String certificationManage() {
		return "hr/certificationManage";
	}
	
	
	
}
