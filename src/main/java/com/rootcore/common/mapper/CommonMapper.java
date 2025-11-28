package com.rootcore.common.mapper;

import com.rootcore.common.vo.CommonVO;

import java.util.List;

public interface CommonMapper {
    List<CommonVO> selectType(String groupCode);
}
