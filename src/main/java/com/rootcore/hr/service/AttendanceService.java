package com.rootcore.hr.service;

import java.util.List;

import com.rootcore.hr.vo.AttendanceVO;

public interface AttendanceService {
	
	// 근태
	List<AttendanceVO> seletMonthAttendance(String month);                 // 월별 전체조회

}
