package com.rootcore.sb.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rootcore.sb.service.CompanyService;
import com.rootcore.sb.service.PlanService;
import com.rootcore.sb.vo.PlanVO;

@Controller
public class ContractController {

	
	@GetMapping("/contract/new")
	public String showContractPage(@RequestParam String companyCode,
	                               @RequestParam String planCode,
	                               @RequestParam Integer subsPeriod,
	                               @RequestParam Integer userCount,
	                               Model model) {
	    CompanyVO company = CompanyService.getCompany(companyCode);
	    PlanVO plan = PlanService.getPlanDetail(planCode);

	    ContractRequestVO contractReq = new ContractRequestVO();
	    contractReq.setCompanyCode(companyCode);
	    contractReq.setPlanCode(planCode);
	    contractReq.setSubsPeriod(subsPeriod);
	    contractReq.setUserCount(userCount);

	    model.addAttribute("company", company);
	    model.addAttribute("plan", plan);
	    model.addAttribute("contract", contractReq);

	    return "contract/contractForm";
	}
}
