package com.rootcore.fi.service;

import java.util.List;
import java.util.Map;

import com.rootcore.fi.vo.MonthSlipVO;
import com.rootcore.fi.vo.SlipMasterVO;

public interface SlipService {
    Map<String, Object> saveManualSlip(SlipMasterVO param);
    List<MonthSlipVO> getMonthSlipList(String yearMonth, String accountCode);

}
