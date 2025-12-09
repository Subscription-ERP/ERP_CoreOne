package com.rootcore.fi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.rootcore.fi.vo.HarpDetailVO;
import com.rootcore.fi.vo.HarpInvoiceVO;
import com.rootcore.fi.vo.HarpMasterVO;

@Mapper
public interface HarpMapper {

    /**
     * 수금 대상 세금계산서 목록 조회
     */
    List<HarpInvoiceVO> selectInvoiceTargetList(HarpInvoiceVO param);

    /**
     * 수금번호 채번
     *  - TB_HARP_MASTER.HARP_NO 기준
     */
    String makeHarpNo();

    /**
     * TB_HARP_MASTER INSERT
     */
    int insertHarpMaster(HarpMasterVO param);

    /**
     * TB_HARP_DETAIL INSERT
     */
    int insertHarpDetail(HarpDetailVO param);
}
