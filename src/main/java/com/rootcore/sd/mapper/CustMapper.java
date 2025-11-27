package com.rootcore.sd.mapper;

import com.rootcore.sd.vo.CustVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustMapper {

    List<CustVO> selectAllCust(CustVO cust);
    int insertCust(CustVO cust);

}
