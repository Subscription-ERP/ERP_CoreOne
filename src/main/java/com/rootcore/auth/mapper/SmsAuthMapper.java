package com.rootcore.auth.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SmsAuthMapper {
    void saveAuthCode(Map<String, Object> param);
    String getValidAuthCode(Map<String, Object> param);
}
