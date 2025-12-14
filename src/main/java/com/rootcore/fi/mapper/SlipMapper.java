package com.rootcore.fi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.fi.vo.MonthSlipVO;
import com.rootcore.fi.vo.SlipDetailVO;
import com.rootcore.fi.vo.SlipMasterVO;

@Mapper
public interface SlipMapper {

    /** 전표번호 생성 (함수 호출) */
    String makeSlipNo();

    /** 전표 마스터 저장 */
    int insertSlipMaster(SlipMasterVO vo);

    /** 전표 디테일 저장 */
    int insertSlipDetail(SlipDetailVO vo);
    
    List<MonthSlipVO> selectMonthSlipList(
    	    @Param("yearMonth") String yearMonth,
    	    @Param("accountCode") String accountCode
    	);
}
