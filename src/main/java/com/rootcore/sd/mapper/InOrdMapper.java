package com.rootcore.sd.mapper;

import com.rootcore.sd.vo.InOrdVO;
import com.rootcore.sd.vo.InOrdDetailVO;

public interface InOrdMapper {
    void selectAllSku(InOrdDetailVO detail);
    void insertInOrd(InOrdVO inOrd);
    void insertInOrdDetail(InOrdDetailVO inOrdDeatil);
}
