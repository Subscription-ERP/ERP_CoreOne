package com.rootcore.common.service;

import com.rootcore.common.vo.CommonVO;

import java.util.List;

public interface CommonService {
    List<CommonVO> selectType(String groupCode);  // 거래처유형
}
