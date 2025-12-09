package com.rootcore.fi.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.fi.mapper.HarpMapper;
import com.rootcore.fi.mapper.SlipMapper;
import com.rootcore.fi.service.HarpService;
import com.rootcore.fi.vo.HarpDetailVO;
import com.rootcore.fi.vo.HarpMasterVO;
import com.rootcore.fi.vo.HarpInvoiceVO;
import com.rootcore.fi.vo.SlipDetailVO;
import com.rootcore.fi.vo.SlipMasterVO;

@Service
public class HarpServiceImpl implements HarpService {

    @Autowired
    private HarpMapper harpMapper;

    @Autowired
    private SlipMapper slipMapper;

    /**
     * 수금 대상 세금계산서 목록 조회
     */
    @Override
    public List<HarpInvoiceVO> selectInvoiceTargetList(HarpInvoiceVO param) {
        return harpMapper.selectInvoiceTargetList(param);
    }

    /**
     * 수금 등록 (TB_HARP_MASTER, TB_HARP_DETAIL, TB_SLIP_MASTER/DETAIL 처리)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveHarp(HarpMasterVO param) throws Exception {

        if (param == null) {
            throw new IllegalArgumentException("요청 데이터가 없습니다.");
        }

        List<HarpDetailVO> details = param.getDetailList();
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("수금 상세 정보가 없습니다.");
        }

        // 1. 수금번호 생성
        String harpNo = harpMapper.makeHarpNo();
        param.setHarpNo(harpNo);

        // 2. 총 수금금액 재계산 (안전하게 서버에서 다시 계산)
        double totalAmount = 0d;
        for (HarpDetailVO d : details) {
            if (d == null) continue;
            Double amt = d.getAmount();
            if (amt == null) continue;
            totalAmount += amt;
        }
        param.setTotalAmount(totalAmount);

        // 3. TB_HARP_MASTER INSERT
        harpMapper.insertHarpMaster(param);

        // 4. TB_HARP_DETAIL INSERT
        int seq = 1;
        for (HarpDetailVO d : details) {
            if (d == null) continue;
            Double amt = d.getAmount();
            if (amt == null || amt <= 0) continue;

            d.setCompanyCode(param.getCompanyCode());
            d.setHarpNo(harpNo);

            // HARP_DETAIL_NO = HARP_NO + 3자리 일련번호 (예: H00001 + 001)
            String harpDetailNo = harpNo + String.format("%03d", seq++);
            d.setHarpDetailNo(harpDetailNo);

            harpMapper.insertHarpDetail(d);

            // (선택) 세금계산서 기준 기수금액/미수금액 갱신이 필요하다면 여기서 처리
            // 예: harpMapper.updateInvoiceHarpAmount(d);
        }

        // 5. 수금 전표 생성 (TB_SLIP_MASTER / TB_SLIP_DETAIL)
        //    - 한 번의 수금(HARP_NO)에 대해 전표 1건 생성
        String slipNo = slipMapper.makeSlipNo();

        SlipMasterVO sm = new SlipMasterVO();
        sm.setCompanyCode(param.getCompanyCode());
        sm.setSlipNo(slipNo);

        // HarpMasterVO 의 harpDate(java.util.Date)를 LocalDate로 변환
        if (param.getHarpDate() != null) {
            LocalDate slipDate = param.getHarpDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            sm.setSlipDate(slipDate);
            sm.setFiscalPeriod(slipDate);
        }

        sm.setCustCode(param.getCustCode());
        // 전표유형은 수금전표에 맞는 코드로 추후 공통코드와 연결 가능 (지금은 예시로 "HARP")
        sm.setSlipType("HARP");
        sm.setSummary("수금 등록 - " + harpNo);
        sm.setStatus("0");
        sm.setInvoiceNo(null);
        sm.setHarpNo(harpNo);

        sm.setDrSum(totalAmount);
        sm.setCrSum(totalAmount);

        slipMapper.insertSlipMaster(sm);

        // 6. TB_SLIP_DETAIL INSERT
        //    (1) 차변: 현금(또는 보통예금) - 총 수금액
        SlipDetailVO sd1 = new SlipDetailVO();
        sd1.setSlipDetailNo(slipNo + "001");
        sd1.setCompanyCode(sm.getCompanyCode());
        sd1.setSlipNo(slipNo);
        sd1.setDrCrType("D");
        sd1.setSlipAccount("101000");  // TODO: 실제 '현금' 또는 '보통예금' 계정코드로 변경
        sd1.setAmount(totalAmount);
        slipMapper.insertSlipDetail(sd1);

        //    (2) 대변: 매출채권 - 총 수금액
        SlipDetailVO sd2 = new SlipDetailVO();
        sd2.setSlipDetailNo(slipNo + "002");
        sd2.setCompanyCode(sm.getCompanyCode());
        sd2.setSlipNo(slipNo);
        sd2.setDrCrType("C");
        sd2.setSlipAccount("110000");  // TODO: 실제 '매출채권(외상매출금)' 계정코드로 변경
        sd2.setAmount(totalAmount);
        slipMapper.insertSlipDetail(sd2);

        // 7. 리턴값 구성
        Map<String, Object> rtn = new HashMap<>();
        rtn.put("harpNo", harpNo);
        rtn.put("slipNo", slipNo);

        return rtn;
    }
}
