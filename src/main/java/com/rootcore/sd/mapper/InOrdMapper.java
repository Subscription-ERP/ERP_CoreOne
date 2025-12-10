package com.rootcore.sd.mapper;

import java.util.List;

import com.rootcore.sd.vo.InOrdDetailVO;
import com.rootcore.sd.vo.InOrdVO;
import org.springframework.data.repository.query.Param;

public interface InOrdMapper {
    void insertInOrd(InOrdVO inOrd);
    void insertInOrdDetail(InOrdDetailVO inOrdDeatil);
    List<InOrdVO> SelectInOrdList(@Param("outputStatusFilter") String outputStatusFilter);
    List<InOrdDetailVO> SelectInOrdDetailList(InOrdDetailVO inOrd);
    void updateInordOutPut(String inOrd);
    void updateInordDetailOutPut(InOrdDetailVO inOrdDeatil);
    void updateInordInvoice(InOrdDetailVO inOrdDeatil);
}
