package com.rootcore.sd.mapper;

import com.rootcore.sd.vo.OutordVO;
import com.rootcore.sd.vo.OutordDetailVO;

public interface OutordMapper {
    void insertOutordHeader(OutordVO info);
    void insertOutordDetail(OutordDetailVO detail);
}
