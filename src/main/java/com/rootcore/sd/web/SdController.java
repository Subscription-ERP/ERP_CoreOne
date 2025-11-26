package com.rootcore.sd.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SdController {

    // 거래처 관리
    @GetMapping("/sd/cust")
    public String custMain() {
        return "sd/cust";
    }

}
