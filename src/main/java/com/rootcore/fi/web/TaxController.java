package com.rootcore.fi.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaxController {

	@GetMapping("/tax/invoice")
	public String taxInvoice() {
	    return "fi/tax/taxInvoice";
	}
}