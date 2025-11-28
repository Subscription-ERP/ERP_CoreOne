package com.rootcore.sb.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rootcore.sb.service.CompanyService;
import com.rootcore.sb.vo.CompanyRequestVO;

@Controller
public class CompanyController {
	private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    
    @GetMapping("/company")
    public String companyForm(Model model) {
        model.addAttribute("companyRequest", new CompanyRequestVO());
        return "sb/company";   // company.html
    }
    
    @PostMapping("/companies")
    public String registerCompany(@ModelAttribute CompanyRequestVO requestVO) {
        String companyCode = companyService.registerCompany(requestVO);
     // GET /{companyCode}/plans 으로 redirect
        return "redirect:/" + companyCode + "/plans";
    }
}
