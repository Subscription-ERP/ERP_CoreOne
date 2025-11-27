package com.rootcore.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface PasswordResetMapper {

    // 1) USER_ID + EMAIL 일치하는지 확인 (비밀번호 재설정 요청 시)
    Map<String, Object> findUserByIdAndEmail(@Param("userId") String userId,
                                             @Param("email") String email);

    // 2) 해당 USER의 기존 토큰 무효화
    int invalidateOldToken(@Param("userId") String userId);

    // 3) 새 토큰 INSERT
    int insertResetToken(Map<String, Object> param);

    // 4) 토큰으로 유효한 레코드 조회 (비밀번호 재설정 화면 진입 시)
    Map<String, Object> findValidToken(@Param("token") String token);

    // 5) USER 테이블의 비밀번호 변경
    int updateUserPassword(@Param("userId") String userId,
                           @Param("password") String password);

    // 6) 토큰 사용 완료 처리(만료시켜버리기)
    int expireToken(@Param("token") String token);
}
