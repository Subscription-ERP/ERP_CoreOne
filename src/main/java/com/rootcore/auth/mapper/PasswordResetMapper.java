package com.rootcore.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface PasswordResetMapper {

    Map<String, Object> findUserByIdAndEmail(
            @Param("userId") String userId,
            @Param("email") String email
    );

    void invalidateOldToken(@Param("userId") String userId);

    void insertResetToken(Map<String, Object> param);

    Map<String, Object> findValidToken(@Param("token") String token);

    int updateUserPassword(
            @Param("companyCode") String companyCode,
            @Param("userId") String userId,
            @Param("password") String password
    );

    void expireToken(@Param("token") String token);
}
