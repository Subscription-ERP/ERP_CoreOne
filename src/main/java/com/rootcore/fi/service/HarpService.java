package com.rootcore.fi.service;

import java.util.List;
import java.util.Map;

import com.rootcore.fi.vo.HarpInvoiceVO;
import com.rootcore.fi.vo.HarpMasterVO;

public interface HarpService {

    /**
     * 수금 대상 세금계산서 목록 조회
     */
    List<HarpInvoiceVO> selectInvoiceTargetList(HarpInvoiceVO param);

    /**
     * 수금 등록 (TB_HARP_MASTER, TB_HARP_DETAIL, 전표 생성/연동)
     * @param param 수금 마스터 + 상세 리스트
     * @return { harpNo, slipNo }
     * @throws Exception
     */
    Map<String, Object> saveHarp(HarpMasterVO param) throws Exception;
}
