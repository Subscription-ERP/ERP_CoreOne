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
    public List<CustVO> getCustList(
            String custCode,
            String custName,
            String custType,
            String custTypeCode,
            Boolean includeStopped) {
        return custMapper.selectCust(
                custCode,
                custName,
                custType,
                custTypeCode,
                includeStopped
        );
    }

    @Override
    public int addCust(CustVO cust) {
        return custMapper.insertCust(cust);
    }

    @Override
    public int modifyCust(CustVO cust) {
        return custMapper.updateCustInfo(cust);
    }

}
