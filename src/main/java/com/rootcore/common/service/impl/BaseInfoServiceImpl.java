package com.rootcore.common.service.impl;

import org.springframework.stereotype.Service;

import com.rootcore.common.mapper.BaseInfoMapper;
import com.rootcore.common.service.BaseInfoService;
import com.rootcore.common.vo.BaseInfoVO;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class BaseInfoServiceImpl implements BaseInfoService {

    private final BaseInfoMapper baseInfoMapper;

	@Override
    public BaseInfoVO getCompany(String companyCode) {
        return baseInfoMapper.getCompany(companyCode);
    }

}
