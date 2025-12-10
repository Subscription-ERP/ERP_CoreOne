package com.rootcore.sd.web;


import java.util.List;

import com.rootcore.sd.vo.InOrdOutPut;
import com.rootcore.sd.vo.InOrdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<InOrdVO> inOrdSearch(@RequestParam(name = "status", required = false, defaultValue = "NOT_DONE")
                                         String statusFilter) {
        return inOrdService.getInOrd(statusFilter);
    }

    // 수주세부사항조회
    @GetMapping("/detail")
    public List<InOrdDetailVO> inOrdDetailSearch(InOrdDetailVO InOrdDetail){
		return inOrdService.getInOrdDetail(InOrdDetail);
    }

    // 수주출고처리
    @PutMapping("/output")
    public void inOrdOutPut(@RequestBody InOrdOutPut req) {
        inOrdService.updateOutputStatus(String.valueOf(req.getInordNo()), req.getDetails());
    }
}
