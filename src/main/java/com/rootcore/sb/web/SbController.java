package com.rootcore.sb.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SbController {
	 // 1단계: 구독정보
    @GetMapping("/step1")
    public String subscribeInfo(Model model) {
        model.addAttribute("page", "subscribe");
        return "sb/sbinfo";
    }

    // 2단계: 사용자정보
    @GetMapping("/step2")
    public String userinfo(@RequestParam String plan, Model model) {
        model.addAttribute("plan", plan); // 선택한 요금제 전달
        return "sb/userinfo"; // step2 템플릿 경로
    }

    // 3단계: 계약서
    @GetMapping("/step3")
    public String contract(Model model) {
        model.addAttribute("page", "subscribe");
        return "sb/contract";
    }

    @GetMapping("/step4")
    public String complete(Model model) {
        model.addAttribute("planName", "Professional");
        model.addAttribute("periodLabel", "3개월");
        model.addAttribute("userCountLabel", "50명");
        model.addAttribute("includedFeatures", "인사, 영업, 회계, 공통");
        model.addAttribute("monthlyFee", "₩13,000");
        model.addAttribute("taxAmount", "₩1,300");
        model.addAttribute("totalAmount", "₩42,900");
        return "sb/payment";
    }
//    // 4단계: 결제완료
//    @GetMapping("/step4")
//    public String complete(Model model) {
//        model.addAttribute("page", "subscribe");
//        return "sb/payment";
//    }
    @GetMapping("/test")
    public String test() {
    	return "sb/PayTest";
    }

}
