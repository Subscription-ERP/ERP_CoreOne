package com.rootcore.hr.service;

import java.util.Date;
import java.util.List;

import com.rootcore.hr.vo.AttendanceVO;

public interface AttendanceService {
	
	// 근태
	List<AttendanceVO> seletMonthAttendance(String month);                 // 월별 전체조회

	// 내근태관리
	List<AttendanceVO> selectMyAttendanceAllList(String userId);                 // 내근태관리 전체조회
	AttendanceVO selectTodayMyAttendance(String userId);                         // 내근태관리 오늘
	List<AttendanceVO> searchMyAttendanceList(String userId, Date startDate, Date endDate); 	// 검색
	void checkinTodayIfNeeded(String companyCode, String userId);          	     // 출근
	int checkoutTodayMyAtt(String companyCode, String userId, String updatedBy); // 퇴근
	int updateTodayWorkPlaceType(String companyCode, String userId, String workPlaceType, String updatedBy);   // 근무형태변경
	
}
