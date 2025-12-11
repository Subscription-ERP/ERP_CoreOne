package com.rootcore.cm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.cm.service.DeptCodeManageService;
import com.rootcore.hr.vo.DeptMasterVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/* ================
 * 부서코드관리 컨트롤러 
 * ================ */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cm")
public class DeptCodeManageRestController {

	private final DeptCodeManageService deptCodeManageService;

	/* ======
	 * 부서조회 
	 * ====== */
	@GetMapping("/selectDeptCodeManage")
	public Map<String, Object> selectDeptCodeManage(DeptMasterVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		param.setCompanyCode(CompanyCode);

		List<DeptMasterVO> list = deptCodeManageService.selectDeptCodeManage(param);

		// RESULT에 실행결과 저장
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);

		// data 내부에 사원조회한거 저장
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);

		// 그럼 RESULT에 실행결과, DATA(사원조회한거) 요렇게 저장되서 넘어감
		result.put("data", data);
		
		return result;
	}
	
	/* ===================
	 * 부서 등록 - 저장버튼기능
	 * =================== */
	@PostMapping("/deptRegister")
	public int insertDeptCodeManage(@RequestBody DeptMasterVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		param.setCompanyCode(CompanyCode);
		
		return deptCodeManageService.insertDeptCodeManage(param);
	}
}
