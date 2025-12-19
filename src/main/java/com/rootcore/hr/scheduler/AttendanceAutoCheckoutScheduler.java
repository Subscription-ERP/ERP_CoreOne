package com.rootcore.hr.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rootcore.hr.mapper.AttendanceMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttendanceAutoCheckoutScheduler {
	
    private final AttendanceMapper attendanceMapper;

    /**
       매일 00:05 실행
       퇴근 미기록(과거) 근태 자동 보정
     */
    @Scheduled(cron = "0 5 0 * * *")
    public void autoCheckoutMissingAttendance() {
    	
    	log.info("[Scheduler] 퇴근 미기록 자동보정 시작");
    	
    	try {
    		int updatedCount = attendanceMapper.autoCheckoutMissingPastAll("SYSTEM");
    		
    		if(updatedCount == 0) {
    			log.info("[Scheduler] 자동보정 대상 없음");
    		}
    		log.info("[Scheduler] 자동보정 실행 완료 - 처리 건수: {}", updatedCount);
    		
    	}catch(Exception e) {
    		// 실패로그
    		log.error("[Scheduler] 퇴근 미기록 자동보정 중 예외 발생", e);
    	}

    }
    
    /*
     	cron 참고 : 
     
	     0 5 0 * * *
		│ │ │ │ │ │
		│ │ │ │ │ └─ 요일
		│ │ │ │ └─── 월
		│ │ │ └───── 일
		│ │ └─────── 시 (0시)
		│ └───────── 분 (5분)
		└─────────── 초
     
     */
    

}
