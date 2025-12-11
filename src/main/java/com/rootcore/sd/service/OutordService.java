package com.rootcore.sd.service;

import com.rootcore.sd.vo.OutordDetailVO;
import com.rootcore.sd.vo.OutordVO;

import java.util.List;

public interface OutordService {
    void addOutord(OutordVO info, List<OutordDetailVO> details);
}
