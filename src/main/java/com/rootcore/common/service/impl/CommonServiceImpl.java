package com.rootcore.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rootcore.common.mapper.CommonMapper;
import com.rootcore.common.service.CommonService;
import com.rootcore.common.vo.CommonVO;

import lombok.RequiredArgsConstructor;

@Service("commonService")
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    final CommonMapper commonMapper;

    @Override
    public List<CommonVO> selectCustType(CommonVO common) {
        return commonMapper.selectCustType(common);
    }

	@Override
	public List<CommonVO> selectCode(String common) {
		return commonMapper.selectCode(common);
	}

	@Override
	public Map<String, List<CommonVO>> selectCodes(String... common) {
		Map<String, List<CommonVO>> map = new HashMap<String, List<CommonVO>>();
		for(String gpCd : common) {
			map.put(gpCd, commonMapper.selectCode(gpCd));
		}
		return map;
	}
}
