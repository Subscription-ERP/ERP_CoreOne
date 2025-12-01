package com.rootcore.sb.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rootcore.sb.service.CompanyService;
import com.rootcore.sb.service.ContractService;
import com.rootcore.sb.service.PlanService;
import com.rootcore.sb.vo.CompanyVO;
import com.rootcore.sb.vo.ContractVO;
import com.rootcore.sb.vo.PlanSelectRequestVO;
import com.rootcore.sb.vo.PlanVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CompanyController {

	private final CompanyService companyService;	//생성자 주입 service 준비상태 실제로 호출하는건 메소드
	private final PlanService planService;
	private final ContractService contractService;
	
	// 1단계 회사 등록화면
	@GetMapping("/company")
	public String companyForm(Model model) {
		model.addAttribute("companyRequest", new CompanyVO());
		return "sb/company"; // company.html
	}

	// 1단계 회사등록 + 플랜선택화면이동
	@PostMapping("/companies")
	public String registerCompany(@ModelAttribute CompanyVO requestVO, HttpSession session) {
		String StringCode = companyService.registerCompany(requestVO);
		session.setAttribute("company", requestVO);
		// GET /{companyCode}/plans 으로 redirect
		return "redirect:/plans";
	}

	/*
	 * 2단계 회사코드로 플랜 선택 
	 * 회사코드로 플랜 선택 화면 진입 예: GET /company/{companyCode}/plans
	 */
	@GetMapping("/plans")
	public String showPlanSelectPage( Model model) {

		List<PlanVO> planList = planService.getPlanList();
		model.addAttribute("planList", planList);

		// /templates/plan/planSelect.html 같은 타임리프 페이지로 매핑
		return "sb/plan";

	}

	/* 2단계 
	 * 플랜 등록 후 계약서 작성 화면으로 이동 (POST: 회사코드 + 플랜코드)
	 */
	@GetMapping("/company/plans/select")
	public String selectPlan(@RequestParam String planCode, RedirectAttributes redirectAttributes, HttpSession session) {
		List<PlanVO> plan = planService.getPlanList();
		session.setAttribute("plan", plan);
		CompanyVO companycode = (CompanyVO)session.getAttribute("companyCode");
		// companyCode, planCode를 계약 작성 화면으로 넘김
		
		
		// 예: /contract/new?companyCode=XXX&planCode=YYY
		return "redirect:/contract/new";
	}
	
	//3단계 계약서 화면
	@GetMapping("/contract/new")
	public String showContractPage( Model model , HttpSession session) {
		//생성자로 주입된 객체를 호출해야함
		CompanyVO company = (CompanyVO)session.getAttribute("company");
		PlanSelectRequestVO planselect = (PlanSelectRequestVO)session.getAttribute("plan");
		PlanVO plan = planService.getPlanDetail(planselect.getPlanCode());

		ContractVO contractReq = new ContractVO();
		contractReq.setPlanCode(planselect.getPlanCode());
		contractReq.setSubsPeriod(planselect.getSubsPeriod());
		contractReq.setUserCount(planselect.getUserCount());

		model.addAttribute("company", company);
		model.addAttribute("plan", plan);
		model.addAttribute("contract", contractReq);

		return "sb/contract";
	}
	//3단계 계약서 등록후 결제페이지 이동
    @GetMapping("/step4")
    public String complete(ContractVO contractVO, Model model, HttpSession session) {
//        model.addAttribute("planName", "Professional");
//        model.addAttribute("periodLabel", "3개월");
//        model.addAttribute("userCountLabel", "50명");
//        model.addAttribute("includedFeatures", "인사, 영업, 회계, 공통");
//        model.addAttribute("monthlyFee", "₩13,000");
//        model.addAttribute("taxAmount", "₩1,300");
//        model.addAttribute("totalAmount", "₩42,900");
        session.setAttribute("contract", contractVO);
        //계약서 등록
        return "sb/payment";
    }
//    // 4단계: 결제완료
//    @GetMapping("/step4")
//    public String complete(Model model) {
//        model.addAttribute("page", "subscribe");
//        return "sb/payment";
//    }
    @PostMapping("/test")
    public String test() {
    	return "sb/PayTest";
    }

}
