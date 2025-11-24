package com.rootcore.tax.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaxController {

	@GetMapping("/tax/invoice")
	public String taxInvoice() {
	    return "tax/taxInvoice";
	}
}