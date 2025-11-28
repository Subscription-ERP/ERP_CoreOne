package com.rootcore.common.service.impl;

import com.rootcore.common.mapper.CommonMapper;
import com.rootcore.common.service.CommonService;
import com.rootcore.common.vo.CommonVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service("commonService")
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    final CommonMapper commonMapper;

    @Override
    public List<CommonVO> selectCustType(CommonVO common) {
        return commonMapper.selectCustType(common);
    }
}
