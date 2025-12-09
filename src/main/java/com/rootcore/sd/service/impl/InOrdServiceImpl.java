package com.rootcore.sd.service.impl;

import com.rootcore.sd.mapper.InOrdMapper;
import com.rootcore.sd.service.InOrdService;
import com.rootcore.sd.vo.InOrdDetailVO;
import com.rootcore.sd.vo.InOrdVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("InOrdService")
@RequiredArgsConstructor
public class InOrdServiceImpl implements InOrdService {

    final InOrdMapper inOrdMapper;

    @Transactional
    public void addInOrd(InOrdVO info, List<InOrdDetailVO> details) {
        inOrdMapper.insertInOrd(info);

        for (InOrdDetailVO detail : details) {
            detail.setInordNo(info.getInordNo());
            inOrdMapper.insertInOrdDetail(detail);
        }
    }

    @Override
    public List<InOrdVO> getInOrd(InOrdVO info) {
        return inOrdMapper.SelectInOrdList(info);
    }

    @Override
	public List<InOrdDetailVO> getInOrdDetail(InOrdDetailVO info) {
		System.out.println(info.getSearchDiv());
		return inOrdMapper.SelectInOrdDetailList(info);
	}

}
