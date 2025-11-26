package com.rootcore.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FindIdMapper {
    String findUserId(@Param("name") String name,
                      @Param("phone") String phone);
}
