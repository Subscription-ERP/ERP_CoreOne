package com.rootcore.hr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.rootcore.hr.vo.AttendanceVO;

@Mapper
public interface AttendanceMapper {

	// 근태
	List<AttendanceVO> seletMonthAttendance(String month);                 // 월별 전체조회
	
	
}
