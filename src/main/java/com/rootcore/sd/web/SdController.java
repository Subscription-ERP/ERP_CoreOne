package com.rootcore.sd.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.rootcore.sd.service.CustService;
import com.rootcore.sd.vo.CustVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class SdController {

    @Autowired
    CustService custService;
    
    /* ======================================================
       거래처
    ====================================================== */

    // 거래처 메인
    @GetMapping("/sd/cust")
    public String custMain(Model model) {
    	model.addAttribute("breadcrumb", List.of(
    	        "영업관리",
    	        "거래처관리"
    	    ));
        return "sd/cust";
    }

    // 거래처 등록
    @GetMapping("/sd/cust/save")
    public String addCustPage() {
        return "/sd/cust/save";
    }

    @PostMapping("/sd/cust/save")
    public String addCust(CustVO cust, HttpSession session) {

        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        String userId      = (String) session.getAttribute("LOGIN_USER_ID");

        cust.setCompanyCode(companyCode);
        cust.setCreatedBy(userId);

        custService.addCust(cust);
        return "redirect:/sd/cust";
    }
    
    // 거래처 수정
    @GetMapping("/sd/cust/modify")
    public String modifyCustPage() {
        return "/sd/cust/modify";
    }

    @PostMapping("/sd/cust/modify")
    public String modifyCust(CustVO cust, HttpSession session) {

        String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
        String userId = (String) session.getAttribute("LOGIN_USER_ID");

        cust.setCompanyCode(companyCode);
        cust.setUpdatedBy(userId);

        custService.modifyCust(cust);
        return "redirect:/sd/cust";
    }

    /* ======================================================
       수주
    ====================================================== */

    // 수주 등록 화면
    @GetMapping("/sd/inord")
    public String inOrdMain(Model model) {
    	model.addAttribute("breadcrumb", List.of(
    	        "영업관리",
    	        "수주등록"
    	    ));
        return "sd/inord";
    }

    // 수주 완료 처리
    @GetMapping("/sd/inordComplete")
    public String inOrdComplete(Model model) {
    	model.addAttribute("breadcrumb", List.of(
    	        "영업관리",
    	        "수주등록 완료처리"
    	    ));
        return "sd/inordComplete";
    }

    /* ======================================================
       발주
    ====================================================== */

    // 발주 등록 화면
    @GetMapping("/sd/outord")
    public String outordMain(Model model) {
    	model.addAttribute("breadcrumb", List.of(
    	        "영업관리",
    	        "발주등록"
    	    ));
        return "sd/outord";
    }

    // 발주 완료 처리
    @GetMapping("/sd/outordComplete")
    public String outordComplete(Model model) {
    	model.addAttribute("breadcrumb", List.of(
    	        "영업관리",
    	        "발주등록 완료처리"
    	    ));
        return "sd/outordComplete";
    }
    
}
