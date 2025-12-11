package com.rootcore.hr.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	// 출근
	@Transactional
	@Override
	public void checkinTodayIfNeeded(String companyCode, String userId) {
		
		// 오늘 근태가 있을 경우(=inTime이 있을 경우) 안함
		AttendanceVO today = attendanceMapper.selectTodayMyAttendance(userId);
		if(today != null && today.getInTime() != null) {
			return;
		}
		
		// 현재 시간 기준으로 정상/지각 판별
		LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
		LocalTime nineAM = LocalTime.of(9, 0);
		
		// h1: 정상, h2: 지각
		String attendType = now.isAfter(nineAM) ? "h2" : "h1"; 
		
		// insert용 vo 구성
		AttendanceVO attendanceVO = new AttendanceVO();
		attendanceVO.setCompanyCode(companyCode);
		attendanceVO.setUserId(userId);
		attendanceVO.setAttendType(attendType);
		attendanceVO.setCreatedBy(userId);
		attendanceVO.setWorkPlaceType("q1");
		attendanceVO.setRemark(null);
		
		attendanceMapper.insertTodayMyAtt(attendanceVO);
				
	}

	
	// 퇴근
	@Transactional
	@Override
	public int checkoutTodayMyAtt(String companyCode, String userId, String updatedBy) {
		
		// 오늘 근태조회
		AttendanceVO today = attendanceMapper.selectTodayMyAttendance(userId);
		if(today == null || today.getInTime() == null) {
			return 0;
		} // 출근기록 없을시 퇴근 처리 X
		
		// 이미 out_time있으면 중복 퇴근 방지
		if(today.getOutTime() != null) {
			return 0;
		}
		
		// 시간 계산 준비
		ZoneId zone = ZoneId.of("Asia/Seoul");
		
		// vo의 date -> LocalDateTime 변환
		LocalDateTime inTime = toLocalDateTime(today.getInTime(), zone);
		LocalDate workDate = inTime.toLocalDate();
		
		LocalDateTime now = LocalDateTime.now(zone); // 퇴근시간
		if(now.isBefore(inTime)) {
			// 이상 상황 방지 : 퇴근시간이 출근보다 앞이면 그냥 같게 맞추기
			now = inTime;
		}
		
		// 기준시간 설정
		LocalDateTime baseStart = LocalDateTime.of(workDate, LocalTime.of(9, 0));
		LocalDateTime baseEnd = LocalDateTime.of(workDate, LocalTime.of(18, 0));
		LocalDateTime overEnd = LocalDateTime.of(workDate, LocalTime.of(22, 0));
		
		// 총 근무시간(출근~퇴근)
		long totalMinutes = Duration.between(inTime, now).toMinutes();
		
		// 연장시간 (18:00 ~ 22:00 구간)
		long overMinutes = 0;
		if(now.isAfter(baseEnd)) {
			LocalDateTime overStart = inTime.isAfter(baseEnd) ? inTime : baseEnd;
			LocalDateTime overActualEnd = now.isBefore(overEnd) ? now : overEnd;
			
			if(overActualEnd.isAfter(overStart)) {
				overMinutes = Duration.between(overStart, overActualEnd).toMinutes();
			}
 		}

		// 야간시간 (22:00 이후)
		long nightMinutes = 0;
		if(now.isAfter(overEnd)) {
			LocalDateTime nightStart = inTime.isAfter(overEnd) ? inTime : overEnd;
	        if (now.isAfter(nightStart)) {
	            nightMinutes = Duration.between(nightStart, now).toMinutes();
	        }
		}
		
		// 분 -> 시간(number(5,2) 맞추기 위해 소수 둘째 자리까지)
		double totalHours = toHours(totalMinutes);
		double overHours = toHours(overMinutes);
		double nightHours = toHours(nightMinutes);
		
		// DB 업데이트 실행
		return attendanceMapper.checkoutTodayMyAtt(
				companyCode, 
		        userId, 
		        updatedBy,
		        overHours,  
		        nightHours,   
		        totalHours);

	}

	private LocalDateTime toLocalDateTime(Date date, ZoneId zone) {
	    return date.toInstant().atZone(zone).toLocalDateTime();
	}
	
	private double toHours(long minutes) {
	    // 분 → 시간, 소수 둘째 자리까지 반올림
	    return BigDecimal.valueOf(minutes / 60.0)
	            .setScale(2, RoundingMode.HALF_UP)
	            .doubleValue();
	}

	// 근무형태 변경
	@Transactional
	@Override
	public int updateTodayWorkPlaceType(String companyCode, String userId, String workPlaceType, String updatedBy) {
		
		// 오늘 근태 조회
		AttendanceVO today = attendanceMapper.selectTodayMyAttendance(userId);
		
		// 출근기록 없으면 변경 X
		if(today == null || today.getInTime() == null) return 0;
		
		// 이미 퇴근한 경우에도 변경 X
		if(today.getOutTime() != null) return 0;
		
		return attendanceMapper.updateTodayWorkPlaceType(companyCode, userId, workPlaceType, updatedBy);
	}


}
