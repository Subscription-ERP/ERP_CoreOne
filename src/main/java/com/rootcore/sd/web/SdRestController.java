package com.rootcore.sd.web;

import com.rootcore.cm.vo.SkuVO;
import com.rootcore.sd.service.CustService;

import com.rootcore.sd.service.InOrdService;
import com.rootcore.sd.vo.CustVO;
import com.rootcore.sd.vo.InOrdSave;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sd")
public class SdRestController {

    @Autowired
    CustService custService;

    @Autowired
    InOrdService inOrdService;

    // 거래처 관련 ===========================================

    @GetMapping("/custList")
    public List<CustVO> custList(CustVO param) {
        return custService.getCustList(param);
    }

    // 거래처 조회조건
    @PostMapping("/searchCust")
    public List<CustVO> searchCust(@RequestBody CustVO param) {
        return custService.getCustList(param);
    }

    // 수주 관련 ============================================

    // 수주 등록
    @PostMapping("/inord/save")
    public void inOrdSave(@RequestBody InOrdSave req) {
        inOrdService.addInOrd(req.getInfo(), req.getDetail());
    }

    // 수주 기본 정보
//    @PostMapping("/inord/headerList")
//    public List<SkuVO> searchHeaderInord(SkuVO param) {
//        return ;
//    }

}