package com.rootcore.sd.service;

import com.rootcore.sd.vo.InOrdDetailVO;
import com.rootcore.sd.vo.InOrdVO;

import java.util.List;

public interface InOrdService {
    void addInOrd(InOrdVO info, List<InOrdDetailVO> details);
    List<InOrdVO> getInOrd(String outputStatusFilter);
    List<InOrdDetailVO> getInOrdDetail(InOrdDetailVO info);
    void updateOutputStatus(String inordNo, List<InOrdDetailVO> details);
}
