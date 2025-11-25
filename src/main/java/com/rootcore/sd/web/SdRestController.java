package com.rootcore.sd.web;

import com.rootcore.sd.service.CustService;
import com.rootcore.sd.vo.CustVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/sd")
public class SdRestController {

    @Autowired
    CustService custService;

    @GetMapping("/cust")
    public List<CustVO> selectCust() {
        return custService.selectCust();
    }
}
