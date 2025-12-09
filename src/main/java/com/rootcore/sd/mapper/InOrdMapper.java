package com.rootcore.sd.mapper;

import java.util.List;

import com.rootcore.sd.vo.InOrdDetailVO;
import com.rootcore.sd.vo.InOrdVO;

public interface InOrdMapper {
    void selectAllSku(InOrdDetailVO detail);
    void insertInOrd(InOrdVO inOrd);
    void insertInOrdDetail(InOrdDetailVO inOrdDeatil);
    List<InOrdVO> SelectInOrdList(InOrdVO inOrd);
    List<InOrdDetailVO> SelectInOrdDetailList(InOrdDetailVO inOrd);
    void updateInordInvoice(InOrdDetailVO inOrdDeatil);
}
