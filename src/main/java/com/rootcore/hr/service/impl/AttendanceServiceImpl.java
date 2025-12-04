package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;

import com.rootcore.hr.mapper.AttendanceMapper;
import com.rootcore.hr.service.AttendanceService;
import com.rootcore.hr.vo.AttendanceVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService  {

	private final AttendanceMapper attendanceMapper;
	
	// 근태 월별 전체조회
	@Override
	public List<AttendanceVO> seletMonthAttendance(String month) {
		return attendanceMapper.seletMonthAttendance(month);
	}

}
