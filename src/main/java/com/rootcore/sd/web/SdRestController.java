package com.rootcore.sd.web;

import com.rootcore.sd.service.CustService;

import com.rootcore.sd.vo.CustVO;
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

    // 거래처 조회조건
    @GetMapping("/custList")
    public List<CustVO> custList(
            @RequestParam(required = false) String custCode,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String custType,
            @RequestParam(required = false) String custTypeCode,
            @RequestParam(required = false) Boolean includeStopped
    ) {
        return custService.getCustList(
                custCode,
                custName,
                custType,
                custTypeCode,
                includeStopped
        );
    }

}