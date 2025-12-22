package com.rootcore.sd.service;

import com.rootcore.sd.vo.InOrdDetailVO;
import com.rootcore.sd.vo.InOrdVO;

import java.util.Date;
import java.util.List;

public interface InOrdService {
    void addInOrd(InOrdVO info, List<InOrdDetailVO> details);
    List<InOrdVO> getInOrd(String outputStatusFilter,
                           String custCode,
                           String custName,
                           Date inordDateFrom,
                           Date inordDateTo);
    List<InOrdDetailVO> getInOrdDetail(InOrdDetailVO info);
    void updateOutputStatus(String inordNo, List<InOrdDetailVO> details);
}
