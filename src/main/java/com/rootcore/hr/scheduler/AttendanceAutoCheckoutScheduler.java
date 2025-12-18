package com.rootcore.hr.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rootcore.hr.mapper.AttendanceMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AttendanceAutoCheckoutScheduler {
	
    private final AttendanceMapper attendanceMapper;

    /**
     * 매일 00:05 실행
     * 퇴근 미기록(과거) 근태 자동 보정
     */
    @Scheduled(cron = "0 5 0 * * *")
    public void autoCheckoutMissingAttendance() {

        attendanceMapper.autoCheckoutMissingPastAll("SYSTEM");

        System.out.println("[Scheduler] 퇴근 미기록 자동보정 실행 완료");
    }
    
    /*
     
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
