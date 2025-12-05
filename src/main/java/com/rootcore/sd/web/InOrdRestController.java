package com.rootcore.sd.web;


import com.rootcore.sd.service.InOrdService;
import com.rootcore.sd.vo.InOrdSave;
import com.rootcore.sd.vo.InOrdVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inOrd")
public class InOrdRestController {

    @Autowired
    InOrdService inOrdService;

    // 수주 등록
    @PostMapping("/save")
    public void inOrdSave(@RequestBody InOrdSave req) {
        inOrdService.addInOrd(req.getInfo(), req.getDetail());
    }
}
