package com.rootcore.fi.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fi")
public class FiController {

	// 세금계산서 발행
	@GetMapping("/taxinvoice")
	public String taxInvoice(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "회계",
		        "세금계산서 발행"
		    ));
	    return "fi/taxInvoice";
	}

	// 단가관리
	@GetMapping("/unitprice")
	public String unitPrice(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "회계",
		        "단가관리"
		    ));
	    return "fi/unitPrice";
	}
	
	// 여신관리
	@GetMapping("/credit")
	public String credit(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "회계",
		        "여신관리"
		    ));
	    return "fi/credit";
	}
	
	// 수금등록
	@GetMapping("/harp")
	public String harp(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "회계",
		        "수금등록"
		    ));
	    return "fi/harp";
	}

	// 세금계산서 발행내역
	@GetMapping("/taxinvoicehistory")
	public String taxInvoiceHistory(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "회계",
		        "세금계산서 발행내역"
		    ));
	    return "fi/taxInvoiceHistory";
	}
	
	// 수동전표등록
	@GetMapping("/slip")
	public String slip(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "회계",
		        "수동전표등록"
		    ));
		return "fi/slip";
	}
	
	// 월별분개장 조회
	@GetMapping("/monthSlip")
	public String monthSlip(Model model) {
		model.addAttribute("breadcrumb", List.of(
		        "회계",
		        "월별분개장 조회"
		    ));
		return "fi/monthSlip";
	}

}