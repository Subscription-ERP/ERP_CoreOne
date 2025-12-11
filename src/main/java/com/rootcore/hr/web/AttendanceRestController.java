package com.rootcore.hr.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public List<AttendanceVO> getAttMonthList(String month) {
		return attendanceService.seletMonthAttendance(month);

	}

	// 내근태관리 -------------------------------------------------------------------
	// 전체조회
	@GetMapping("/my")
	public List<AttendanceVO> getMyAtt(HttpSession session) {
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
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		String UserId = (String) session.getAttribute("LOGIN_USER_ID");
		return attendanceService.searchMyAttendanceList(UserId, startDate, endDate);
	}

	// 퇴근
	@PostMapping("/my/checkout")
	public Map<String, Object> checkoutToday(HttpSession session) {

		String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		String userId = (String) session.getAttribute("LOGIN_USER_ID");

		int updated = attendanceService.checkoutTodayMyAtt(companyCode, userId, userId);

		Map<String, Object> res = new HashMap<>();
		if (updated > 0) {
			res.put("success", true);
			res.put("message", "퇴근 처리가 완료되었습니다.");
		} else {
			res.put("success", false);
			res.put("message", "이미 퇴근했거나 출근 기록이 없습니다.");
		}
		return res;
	}

	// 근무형태 변경
	@PostMapping("/my/workPlace")
	public Map<String, Object> changeWorkPlace(@RequestBody Map<String, String> payload, HttpSession session) {

		String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		String userId = (String) session.getAttribute("LOGIN_USER_ID");
		String workPlaceType = payload.get("workPlaceType"); // q1, q2 이런 값
		
		int updated = attendanceService.updateTodayWorkPlaceType(companyCode, userId, workPlaceType, userId);
		
		Map<String, Object> res = new HashMap<>();
		if(updated > 0) {
			res.put("success", true);
		}else {
			res.put("success", false);
		}

		return res;
	}

}
