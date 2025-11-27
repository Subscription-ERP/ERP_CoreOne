package com.rootcore.sd.web;

import com.rootcore.sd.service.CustService;
import com.rootcore.sd.vo.CustVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SdController {

    @Autowired
    CustService custService;

    // 거래처 관리
    @GetMapping("/sd/cust")
    public String custMain() {
        return "sd/cust";
    }

    // 거래처 등록
    @GetMapping("/sd/addCust")
    public String addCustPage() {
        return "sd/addCust";
    }

    @PostMapping("/sd/addCust")
    public String addCust(CustVO cust) {
        custService.addCust(cust);
        return "redirect:/sd/cust";
    };

}
