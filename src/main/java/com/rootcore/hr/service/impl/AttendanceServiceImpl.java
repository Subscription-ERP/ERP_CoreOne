package com.rootcore.hr.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.hr.mapper.AttendanceMapper;
import com.rootcore.hr.service.AttendanceService;
import com.rootcore.hr.vo.AttendanceVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService  {

	private final AttendanceMapper attendanceMapper;
	
	// 근태관리 ---------------------------------------------------------------
	// 근태 월별 전체조회
	@Override
	public List<AttendanceVO> seletMonthAttendance(String month) {
		return attendanceMapper.seletMonthAttendance(month);
	}

	// 내근태관리 --------------------------------------------------------------
	// 내근태관리 전체조회
	@Override
	public List<AttendanceVO> selectMyAttendanceAllList(String userId) {
		return attendanceMapper.selectMyAttendanceAllList(userId);
	}

	// 내근태관리 오늘
	@Override
	public AttendanceVO selectTodayMyAttendance(String userId) {
		return attendanceMapper.selectTodayMyAttendance(userId);
	}

	// 검색
	@Override
	public List<AttendanceVO> searchMyAttendanceList(String userId, Date startDate, Date endDate) {
		return attendanceMapper.searchMyAttendanceList(userId, startDate, endDate);
	}

}
