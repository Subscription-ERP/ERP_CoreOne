package com.rootcore.hr.web;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import com.rootcore.hr.service.AttendanceService;
import com.rootcore.hr.vo.AttendanceVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/att")
public class AttendanceRestController {

	private final AttendanceService attendanceService;
	
	// 근태관리 ---------------------------------------------------------------------
	// 근태 월별 조회
	@GetMapping("/attMonthList")
	public List<AttendanceVO> getAttMonthList(String month){
		return attendanceService.seletMonthAttendance(month);
		
	}

	
	// 내근태관리 -------------------------------------------------------------------
	// 전체조회
	@GetMapping("/my")
	public List<AttendanceVO> getMyAtt(HttpSession session){
		String UserId = (String) session.getAttribute("LOGIN_USER_ID");		
		return attendanceService.selectMyAttendanceAllList(UserId);
	}
	
	// 내근태관리 오늘
	@GetMapping("/my/today")
	public AttendanceVO getMyAttToday(HttpSession session) {
		String UserId = (String) session.getAttribute("LOGIN_USER_ID");	
		return attendanceService.selectTodayMyAttendance(UserId);
	}
	
	// 검색
	@GetMapping("/my/search")
	public List<AttendanceVO> searchMyAtt(HttpSession session,
	        @RequestParam(required = false)
	        @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
	        @RequestParam(required = false)
	        @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
		String UserId = (String) session.getAttribute("LOGIN_USER_ID");	
		return attendanceService.searchMyAttendanceList(UserId, startDate, endDate);
	}
	
}
