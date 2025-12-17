package com.rootcore.sd.mapper;

import com.rootcore.sd.vo.CustVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface CustMapper {

    List<CustVO> selectCust(
            @Param("custCode") String custCode,
            @Param("custName") String custName,
            @Param("custType") String custType,
            @Param("custTypeCode") String custTypeCode,
            @Param("includeStopped") Boolean includeStopped
    );
    int insertCust(CustVO cust);
    int updateCustInfo(CustVO cust);

}
