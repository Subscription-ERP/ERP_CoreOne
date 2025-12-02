package com.rootcore.sb.web;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rootcore.sb.service.CompanyService;
import com.rootcore.sb.service.ContractService;
import com.rootcore.sb.service.PlanService;
import com.rootcore.sb.vo.CompanyVO;
import com.rootcore.sb.vo.ContractVO;
import com.rootcore.sb.vo.PlanVO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CompanyController {

	private final CompanyService companyService; // 생성자 주입 service 준비상태 실제로 호출하는건 메소드
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
//		String StringCode = companyService.registerCompany(requestVO);
		session.setAttribute("company", requestVO);
		// GET /{companyCode}/plans 으로 redirect
		return "redirect:/plans";
	}

	/*
	 * 2단계 회사코드로 플랜 선택 회사코드로 플랜 선택 화면 진입 예: GET /company/{companyCode}/plans
	 */
	@GetMapping("/plans")
	public String showPlanSelectPage(Model model) {

		List<PlanVO> planList = planService.getPlanList();
		model.addAttribute("planList", planList);

		// /templates/plan/planSelect.html 같은 타임리프 페이지로 매핑
		return "sb/plan";

	}

	/*
	 * 2단계 플랜 조회 후 계약서 작성 화면으로 이동 (POST: 회사코드 + 플랜코드)
	 */
	@GetMapping("/company/plans/select") // 화면에서 넘겨준값이 플랜코드,기간,사용자수,회사코드 나머지 필드는 null값인 상태
	public String selectPlan(PlanVO plan, RedirectAttributes redirectAttributes, HttpSession session) { 
		PlanVO result = planService.getPlanDetail(plan.getPlanCode()); //넘어온 플랜코드로 단건조회
		//DB에서 조회한 데이터 복사
		plan.setPrice(result.getPrice());	
		plan.setPlanName(result.getPlanName());
		plan.setPlanInfo(result.getPlanInfo());
		
		session.setAttribute("plan", plan);
		//세션에는 플랜정보,가격,플랜명,플랜코드,기간,사용자수,회사코드이 담겨져있음
		// companyCode, planCode를 계약 작성 화면으로 넘김

		// 예: /contract/new?companyCode=XXX&planCode=YYY
		return "redirect:/contract/new";
	}

	// 3단계 계약서 화면
	@GetMapping("/contract/new")
	public String showContractPage(Model model, HttpSession session) {
		// 생성자로 주입된 객체를 호출해야함
		CompanyVO company = (CompanyVO) session.getAttribute("company");
		PlanVO plan = (PlanVO) session.getAttribute("plan");

		BigDecimal vat = planService.calculateVat(plan);
		BigDecimal totalPrice = planService.calculateTotalPrice(plan);

		ContractVO contractReq = new ContractVO();
		contractReq.setPlanCode(plan.getPlanCode());
		contractReq.setSubsPeriod(plan.getSubsPeriod());
		contractReq.setUserCount(plan.getUserCount());
		contractReq.setTotalPrice(totalPrice);
		contractReq.setVat(vat);

		session.setAttribute("contract", contractReq);	

		model.addAttribute("company", company);
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("plan", plan);
		model.addAttribute("contract", contractReq);

		return "sb/contract";
	}

	// 4단계 계약서 등록후 결제페이지 이동
	@GetMapping("/payment")
	public String complete(Model model, HttpSession session) {
		// 계약서 등록
		model.addAttribute("tossClientKey", "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm");
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
