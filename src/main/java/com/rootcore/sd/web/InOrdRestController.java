package com.rootcore.sd.web;


import java.util.List;

import com.rootcore.sd.vo.InOrdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.sd.service.InOrdService;
import com.rootcore.sd.vo.InOrdDetailVO;
import com.rootcore.sd.vo.InOrdSave;

import lombok.RequiredArgsConstructor;

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

    // 수주헤더조회
    @GetMapping("/info")
    public List<InOrdVO>ninOrdSearch(InOrdVO inOrd) {
        return inOrdService.getInOrd(inOrd);
    }

    // 수주세부사항조회
    @GetMapping("/detail")
    public List<InOrdDetailVO> inOrdDetailSearch(InOrdDetailVO InOrdDetail){
		return inOrdService.getInOrdDetail(InOrdDetail);
    }

}
