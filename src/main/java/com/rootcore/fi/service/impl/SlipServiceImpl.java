package com.rootcore.fi.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.fi.mapper.SlipMapper;
import com.rootcore.fi.service.SlipService;
import com.rootcore.fi.vo.MonthSlipVO;
import com.rootcore.fi.vo.SlipDetailVO;
import com.rootcore.fi.vo.SlipMasterVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlipServiceImpl implements SlipService {

    private final SlipMapper slipMapper;

    /**
     * 수동 전표 등록
     * 1) 전표번호 생성
     * 2) 차/대변 합계 계산
     * 3) SLIP_MASTER 저장
     * 4) SLIP_DETAIL 다건 저장
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveManualSlip(SlipMasterVO slip) {

        // ============================
        // 1. 전표번호 생성
        // ============================
        String slipNo = slipMapper.makeSlipNo();
        slip.setSlipNo(slipNo);

        // ============================
        // 2. 기본값 세팅 (전표일자, 회계기간, 상태 등)
        // ============================
        LocalDate slipDate = slip.getSlipDate();
        if (slipDate == null) {
            slipDate = LocalDate.now();
            slip.setSlipDate(slipDate);
        }

        // 회계기간이 별도로 없으면 전표일자 기준으로 설정
        if (slip.getFiscalPeriod() == null) {
            slip.setFiscalPeriod(slipDate);
        }

        // 상태값 기본값(미결 등)을 '0'으로 사용한다고 가정
        if (slip.getStatus() == null || slip.getStatus().isBlank()) {
            slip.setStatus("0");
        }

        // ============================
        // 3. 차/대변 합계 계산
        //    - SlipMasterVO 에 detailList<List<SlipDetailVO>> 필드가 있어야 합니다.
        // ============================
        List<SlipDetailVO> details = slip.getDetailList();

        double drSum = 0d;
        double crSum = 0d;

        if (details != null) {
            for (SlipDetailVO d : details) {
                // 차변 / 대변 구분
                if ("D".equals(d.getDrCrType())) {
                    drSum += d.getAmount();
                } else if ("C".equals(d.getDrCrType())) {
                    crSum += d.getAmount();
                }
            }
        }

        slip.setDrSum(drSum);
        slip.setCrSum(crSum);

        // 차대변 금액이 반드시 일치해야 한다면 아래 검증을 활성화
        // if (Double.compare(drSum, crSum) != 0) {
        //     throw new RuntimeException("차변/대변 금액이 일치하지 않습니다.");
        // }

        // ============================
        // 4. SLIP_MASTER INSERT
        // ============================
        slipMapper.insertSlipMaster(slip);

        // ============================
        // 5. SLIP_DETAIL INSERT
        // ============================
        if (details != null && !details.isEmpty()) {

            int seq = 1;

            for (SlipDetailVO d : details) {

                // 공통 키 세팅
                d.setCompanyCode(slip.getCompanyCode());
                d.setSlipNo(slipNo);
                d.setSlipDetailNo(String.format("%s-%03d", slipNo, seq++));

                // 등록자 / 수정자 공통 사용 시
                d.setCreatedBy(slip.getCreatedBy());
                d.setUpdatedBy(slip.getUpdatedBy());

                slipMapper.insertSlipDetail(d);
            }
        }

        // ============================
        // 6. 결과 반환
        // ============================
        Map<String, Object> result = new HashMap<>();
        result.put("slipNo", slipNo);
        result.put("drSum", drSum);
        result.put("crSum", crSum);

        return result;
    }

    @Override
    public List<MonthSlipVO> getMonthSlipList(String yearMonth, String accountCode) {
        return slipMapper.selectMonthSlipList(yearMonth, accountCode);
    }

}
