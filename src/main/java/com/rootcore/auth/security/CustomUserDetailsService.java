package com.rootcore.auth.security;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rootcore.auth.mapper.LoginMapper;
import com.rootcore.auth.vo.LoginVO;
import com.rootcore.hr.mapper.AttendanceMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginMapper loginMapper;
    private final AttendanceMapper attendanceMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        // 메서드명 변경됨(findByUserId)
        LoginVO loginVO = loginMapper.findByUserId(userId);

        if (loginVO == null) {
            log.warn("로그인 실패 - 사용자 없음 userId={}", userId);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        
        // 오늘 연차(h5)일 경우 로그인 자체 차단
        int cnt = attendanceMapper.countAnnualLeaveToday(userId);
        if(cnt > 0){
        	throw new LockedException("ANNUAL_LEAVE_TODAY");
        }

        return new SecurityUser(loginVO);
    }
}