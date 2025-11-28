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

    // 거래처 메인
    @GetMapping("/sd/cust")
    public String custMain() {
        return "sd/cust";
    }

    // 거래처 등록
    @GetMapping("/sd/cust/save")
    public String addCustPage() {
        return "/sd/cust/save";
    }

    @PostMapping("/sd/cust/save")
    public String addCust(CustVO cust) {
        custService.addCust(cust);
        return "redirect:/sd/cust";
    };
    
    // 거래처 수정
    @GetMapping("/sd/cust/modify")
    public String modifyCustPage() {
        return "/sd/cust/modify";
    }

    @PostMapping("/sd/cust/modify")
    public String modifyCust(CustVO cust) {
        custService.modifyCust(cust);
        return "redirect:/sd/cust";
    }

}
