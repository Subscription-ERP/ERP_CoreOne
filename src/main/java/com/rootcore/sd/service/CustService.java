package com.rootcore.sd.service;

import com.rootcore.sd.vo.CustVO;

import java.util.List;

public interface CustService {
    List<CustVO> getCustList(
            String custCode,
            String custName,
            String custType,
            String custTypeCode,
            Boolean includeStopped
    );
    int addCust(CustVO cust);
    int modifyCust(CustVO cust);
}
