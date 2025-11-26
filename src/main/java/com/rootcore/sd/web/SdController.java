package com.rootcore.sd.web;

import com.rootcore.sd.service.CustService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SdController {

    @Autowired
    CustService custService;

    // 거래처 관리
    @GetMapping("/sd/cust")
    public String custMain() {
        return "sd/cust";
    }

    // 거래처 모달

}
