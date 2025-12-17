package com.rootcore.auth.mapper;

import com.rootcore.auth.vo.LoginVO;
import com.rootcore.auth.vo.LoginUserVO;   
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {

	
	// ✅ 회사코드 + 사용자 ID
    LoginUserVO selectLoginUser(
            @Param("companyCode") String companyCode,
            @Param("userId") String userId
    );
    
	// 로그인 사용자 조회 (회사코드 포함) 
    LoginVO findByUserIdAndCompany(
            @Param("companyCode") String companyCode,
            @Param("userId") String userId
    );
	
    //	1) 로그인 사용자 조회 
    LoginVO findByUserId(@Param("userId") String userId);

    //	2) 현재 실패 횟수 가져오기 
    int getFailCount(@Param("userId") String userId);

    //	3) 실패 횟수 증가 
    int increaseFailCount(@Param("userId") String userId);

    //	4) 실패 횟수 직접 업데이트 
    int updateFailCount(@Param("userId") String userId,
                        @Param("failCount") int failCount);

    //	5) 계정 잠금
    int lockUserAccount(@Param("userId") String userId);

    //6) 성공 로그인 - 실패횟수 0 
    int resetFailCount(@Param("userId") String userId);

    //	7) 성공 로그인 - 마지막 로그인 업데이트 
    int updateLastLogin(@Param("userId") String userId);

    //	8) 성공 로그인 - 실패 0 + 마지막 로그인 동시에 
    int resetFailCountAndLastLogin(@Param("userId") String userId);

    //	9) 계정 잠금 해제 
    int unlockUserAccount(@Param("userId") String userId);

    
    //  10) 로그인 성공 후 세션 저장용 사용자 정보 조회
    LoginUserVO selectLoginUser(@Param("userId") String userId);
}
