package com.rootcore.sd.web;


import java.util.Date;
import java.util.List;

import com.rootcore.sd.vo.InOrdOutPut;
import com.rootcore.sd.vo.InOrdVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.rootcore.sd.service.InOrdService;
import com.rootcore.sd.vo.InOrdDetailVO;
import com.rootcore.sd.vo.OrdSave;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inOrd")
public class InOrdRestController {

    @Autowired
    InOrdService inOrdService;

    // 수주 등록
    @PostMapping("/save")
    public void inOrdSave(@RequestBody OrdSave req, HttpSession session) {

        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        String userId = (String) session.getAttribute("LOGIN_USER_ID");

        InOrdVO inordInfo = req.getInordInfo();
        inordInfo.setCompanyCode(companyCode);
        inordInfo.setCreatedBy(userId);

        List<InOrdDetailVO> inordDetail = req.getInordDetail();
        for (InOrdDetailVO detail : inordDetail) {
            detail.setCompanyCode(companyCode);
            detail.setCreatedBy(userId);
        }

        inOrdService.addInOrd(inordInfo, inordDetail);
    }

    // 수주헤더조회
    @GetMapping("/info")
    public List<InOrdVO> inOrdSearch(
            @RequestParam(name = "status", required = false, defaultValue = "NOT_DONE") String statusFilter,
            @RequestParam(name = "custCode", required = false) String custCode,
            @RequestParam(name = "custName", required = false) String custName,
            @RequestParam(name = "inordDateFrom", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date inordDateFrom,
            @RequestParam(name = "inordDateTo", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date inordDateTo
    ) {
        return inOrdService.getInOrd(statusFilter, custCode, custName, inordDateFrom, inordDateTo);
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
