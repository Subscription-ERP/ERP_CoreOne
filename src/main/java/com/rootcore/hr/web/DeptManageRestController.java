package com.rootcore.hr.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.DeptManageService;
import com.rootcore.hr.vo.DeptMasterVO;
import com.rootcore.hr.vo.UserVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class DeptManageRestController {
	private final DeptManageService deptManageService;

	// 조직도관리-부서조회
	@GetMapping("/deptStructure")
	public List<DeptMasterVO> selectDeptList(DeptMasterVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		param.setCompanyCode(CompanyCode);
		
		return deptManageService.selectDeptList(param);
	}

	// 조직도관리-사원조회
	@GetMapping("/deptUserList")
	public Map<String, Object> selectDeptUserList(DeptMasterVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		param.setCompanyCode(CompanyCode);
		
		List<UserVO> list = deptManageService.selectDeptUserList(param);
		
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
}
