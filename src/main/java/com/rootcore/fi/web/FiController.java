package com.rootcore.fi.web;

import java.util.List;

import org.springframework.ai.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rootcore.fi.vo.TaxInvoiceVO;

@Controller
@RequestMapping("/fi")
public class FiController {

	@GetMapping("/taxinvoice")
	public String taxInvoice() {
	    return "fi/taxInvoice";
	}

	@GetMapping("/unitprice")
	public String unitPrice() {
	    return "fi/unitPrice";
	}
	
	@GetMapping("/credit")
	public String credit() {
	    return "fi/credit";
	}
	
	@GetMapping("/harp")
	public String harp() {
	    return "fi/harp";
	}

	@GetMapping("/taxinvoicehistory")
	public String taxInvoiceHistory() {
	    return "fi/taxInvoiceHistory";
	}
	@GetMapping("/slip")
	public String slip() {
		return "fi/slip";
	}
	@GetMapping("/monthSlip")
	public String monthSlip() {
		return "fi/monthSlip";
	}

}