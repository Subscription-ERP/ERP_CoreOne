package com.rootcore.fi.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class FiPageController {

    @GetMapping("/fi/unit_price")
    public String unitPrice() { return "fi/unit_price"; }

    @GetMapping("/fi/tax_invoice")
    public String taxInvoice() { return "fi/tax_invoice"; }

    @GetMapping("/fi/tax_invoice/list")
    public String taxInvoiceList() { return "fi/tax_invoice_list"; }

    @GetMapping("/fi/tax_invoice/print")
    public String taxInvoicePrint() { return "fi/tax_invoice_print"; }

    @GetMapping("/fi/manual_journal")
    public String manualJournal() { return "fi/manual_journal"; }

    @GetMapping("/fi/journal/monthly")
    public String journalMonthly() { return "fi/journal_monthly"; }

}
