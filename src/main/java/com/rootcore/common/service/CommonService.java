package com.rootcore.common.service;

import com.rootcore.common.vo.CommonVO;

import java.util.List;

public interface CommonService {
    List<CommonVO> selectCustType(CommonVO common);  // 거래처유형
}
