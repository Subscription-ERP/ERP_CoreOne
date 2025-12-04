package com.rootcore.hr.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.AttendanceService;
import com.rootcore.hr.vo.AttendanceVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/att")
public class AttendanceRestController {

	private final AttendanceService attendanceService;
	
	// 근태 월별 조회
	@GetMapping("/attMonthList")
	public List<AttendanceVO> getAttMonthList(String month){
		return attendanceService.seletMonthAttendance(month);
	}

	
}
