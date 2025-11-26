package com.rootcore.sd.web;

import com.rootcore.fi.vo.UnitPriceVO;
import com.rootcore.sd.service.CustService;
import com.rootcore.sd.vo.CustVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sd")
public class SdRestController {

    @Autowired
    CustService custService;

    @GetMapping("/cust")
    public List<CustVO> getCustList(CustVO param) {
        return custService.selectCust(param);
    }
}