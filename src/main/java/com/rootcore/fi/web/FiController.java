package com.rootcore.fi.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fi")
public class FiController {

	@GetMapping("/taxinvoice")
	public String taxInvoice() {
	    return "fi/taxInvoice";
	}

	@GetMapping("/unitprice")
	public String unitPrice() {
	    return "fi/unitprice";
	}
	
	@GetMapping("/credit")
	public String credit() {
	    return "fi/credit";
	}
	
	@GetMapping("/harp")
	public String harp() {
	    return "fi/harp";
	}
}