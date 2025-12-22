package com.rootcore.sd.web;

import com.rootcore.sd.service.OutordService;
import com.rootcore.sd.vo.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/outord")
public class OutordRestController {

    @Autowired
    OutordService outordService;
    
    // 발주 등록
    @PostMapping("/save")
    public void outordSave(@RequestBody OrdSave req, HttpSession session) {

        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        String userId = (String) session.getAttribute("LOGIN_USER_ID");

        OutordVO outordInfo = req.getOutordInfo();
        outordInfo.setCompanyCode(companyCode);
        outordInfo.setCreatedBy(userId);

        List<OutordDetailVO> outordDetail = req.getOutordDetail();
        for (OutordDetailVO detail : outordDetail) {
            detail.setCompanyCode(companyCode);
            detail.setCreatedBy(userId);
        }

        outordService.addOutord(outordInfo, outordDetail);

    }
}
