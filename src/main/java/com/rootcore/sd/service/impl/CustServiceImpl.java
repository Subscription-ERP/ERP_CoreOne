package com.rootcore.sd.service.impl;

import com.rootcore.sd.mapper.CustMapper;
import com.rootcore.sd.service.CustService;
import com.rootcore.sd.vo.CustVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CustService")
@RequiredArgsConstructor
public class CustServiceImpl implements CustService {

    final CustMapper custMapper;

    @Override
    public List<CustVO> getCustList(CustVO cust) {
        return custMapper.selectAllCust(cust);
    }

    @Override
    public int addCust(CustVO cust) {
        return custMapper.insertCust(cust);
    }

}
