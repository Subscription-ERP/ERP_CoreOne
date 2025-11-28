package com.rootcore.sb.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rootcore.sb.service.PlanService;
import com.rootcore.sb.vo.PlanSelectRequestVO;
import com.rootcore.sb.vo.PlanVO;

@Controller
public class PlanController {
	 private final PlanService planService;

	    public PlanController(PlanService planService) {
	        this.planService = planService;
	    }
	    
	    /**
	     * 회사코드로 플랜 선택 화면 진입
	     * 예: GET /company/{companyCode}/plans
	     */
	    @GetMapping("/{companyCode}/plans")
	    public String showPlanSelectPage(@PathVariable("companyCode") String companyCode,
	                                     Model model) {

	        List<PlanVO> planList = planService.getPlanList();

	        model.addAttribute("companyCode", companyCode);
	        model.addAttribute("planList", planList);

	        // /templates/plan/planSelect.html 같은 타임리프 페이지로 매핑
	        return "sb/plan";
	        
	        
	    }
	    /**
	     * 플랜 선택 후 계약서 작성 화면으로 이동
	     * (POST: 회사코드 + 플랜코드)
	     */
	    @PostMapping("/company/plans/select")
	    public String selectPlan(PlanSelectRequestVO requestVO,
	                             RedirectAttributes redirectAttributes) {

	        // companyCode, planCode를 계약 작성 화면으로 넘김
	        redirectAttributes.addAttribute("companyCode", requestVO.getCompanyCode());
	        redirectAttributes.addAttribute("planCode", requestVO.getPlanCode());
	        redirectAttributes.addAttribute("subsPeriod", requestVO.getPlanCode());
	        redirectAttributes.addAttribute("userCount", requestVO.getPlanCode());

	        // 예: /contract/new?companyCode=XXX&planCode=YYY
	        return "redirect:/contract/new";
	    }
}
