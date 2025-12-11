package com.rootcore.sd.web;

import com.rootcore.sd.service.OutordService;
import com.rootcore.sd.vo.OrdSave;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/outord")
public class OutordRestController {

    @Autowired
    OutordService outordService;
    
    // 발주 등록
    @PostMapping("/save")
    public void outordSave(@RequestBody OrdSave req) {
        outordService.addOutord(req.getOutordInfo(), req.getOutordDetail());
    }
}
