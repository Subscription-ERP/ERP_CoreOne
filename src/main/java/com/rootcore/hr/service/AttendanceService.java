package com.rootcore.hr.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rootcore.hr.vo.AttendanceVO;

public interface AttendanceService {
	
	// 근태
	List<AttendanceVO> seletMonthAttendance(String month);                 // 월별 전체조회

	// 내근태관리
	List<AttendanceVO> selectMyAttendanceAllList(String userId);           // 내근태관리 전체조회
	AttendanceVO selectTodayMyAttendance(String userId);                   // 내근태관리 오늘
	
	// 검색
	List<AttendanceVO> searchMyAttendanceList(@Param("userId") String userId,
			                                  @Param("startDate") Date startDate,
				                              @Param("endDate") Date endDate);
	
	// 퇴근
	/*
	 * int checkoutTodayMyAtt(@Param("companyCode") String companyCode,
	 * 
	 * @Param("userId") String userId,
	 * 
	 * @Param("updatedBy") String updatedBy);
	 */
	
	
}
