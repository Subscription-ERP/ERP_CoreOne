package com.rootcore.sd.mapper;

import java.util.Date;
import java.util.List;

import com.rootcore.sd.vo.InOrdDetailVO;
import com.rootcore.sd.vo.InOrdVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface InOrdMapper {
    void insertInOrd(InOrdVO inOrd);
    void insertInOrdDetail(InOrdDetailVO inOrdDeatil);
    List<InOrdVO> SelectInOrdList(InOrdVO param);
    List<InOrdDetailVO> SelectInOrdDetailList(InOrdDetailVO inOrd);
    void updateInordOutPut(String inOrd);
    void updateInordDetailOutPut(InOrdDetailVO inOrdDeatil);
    void updateInordInvoice(InOrdDetailVO inOrdDeatil);

}
