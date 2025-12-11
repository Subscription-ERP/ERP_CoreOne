package com.rootcore.sd.service.impl;

import com.rootcore.sd.mapper.OutordMapper;
import com.rootcore.sd.service.OutordService;
import com.rootcore.sd.vo.OutordDetailVO;
import com.rootcore.sd.vo.OutordVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("OutordService")
@RequiredArgsConstructor
public class OutordServiceImpl implements OutordService {

    final OutordMapper outordMapper;

    @Override
    @Transactional
    public void addOutord(OutordVO info, List<OutordDetailVO> details) {
        outordMapper.insertOutordHeader(info);

        for (OutordDetailVO detail : details) {
            detail.setOutordNo(info.getOutordNo());
            outordMapper.insertOutordDetail(detail);
        }
    }
}
