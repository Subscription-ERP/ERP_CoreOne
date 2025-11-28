package com.rootcore.common.service;

import com.rootcore.common.vo.CommonVO;

import java.util.List;
import java.util.Map;

public interface CommonService {
    List<CommonVO> selectCustType(CommonVO common);  // 거래처유형
    List<CommonVO> selectCode(String common); //공통코드
    Map<String, List<CommonVO>> selectCodes(String ... common); //공통코드
}
