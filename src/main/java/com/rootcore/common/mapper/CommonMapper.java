package com.rootcore.common.mapper;

import com.rootcore.common.vo.CommonVO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {
    List<CommonVO> selectCode(String common);
    List<CommonVO> selectType(String groupCode);
}
