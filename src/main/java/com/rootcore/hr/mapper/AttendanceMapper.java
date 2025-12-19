package com.rootcore.hr.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.hr.vo.AttendanceVO;

@Mapper
public interface AttendanceMapper {

	// 근태관리
	List<AttendanceVO> seletMonthAttendance(String month);                 // 월별 전체조회
	int countAnnualLeaveToday(String userId);                              // 연차(h5)로그인막기 조회
	
	// 내근태관리 -----------------------------------------------------------------------------
	List<AttendanceVO> selectMyAttendanceAllList(String userId);           // 내근태관리 전체조회
	AttendanceVO selectTodayMyAttendance(String userId);                   // 내근태관리 오늘
	
	// 검색
	List<AttendanceVO> searchMyAttendanceList(@Param("userId") String userId,
			                                  @Param("startDate") Date startDate,
				                              @Param("endDate") Date endDate);
	
	// 출근
	int insertTodayMyAtt(AttendanceVO vo);
	
	// 퇴근
	int checkoutTodayMyAtt(@Param("companyCode") String companyCode,
	                       @Param("userId") String userId,
	                       @Param("updatedBy") String updatedBy,
	                       @Param("overWorkTime")   double overWorkTime,
	                       @Param("nightWorkTime")  double nightWorkTime,
	                       @Param("totalWorkTime")  double totalWorkTime);
	
	// 근무형태 변경 
	int updateTodayWorkPlaceType(@Param("companyCode")  String companyCode,
	                             @Param("userId")       String userId,
	                             @Param("workPlaceType") String workPlaceType,
	                             @Param("updatedBy")    String updatedBy);
	
	// 자동보정
	int autoCheckoutMissingPastAll(@Param("updatedBy") String updatedBy);
	
	
}
