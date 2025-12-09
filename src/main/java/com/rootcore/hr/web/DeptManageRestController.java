package com.rootcore.hr.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.DeptManageService;
import com.rootcore.hr.vo.DeptMasterVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class DeptManageRestController {
	private final DeptManageService deptManageService;
	
	// 조직도관리-부서조회
	@GetMapping("/deptStructure")
	public List<DeptMasterVO> selectDeptList(DeptMasterVO param) {
		return deptManageService.selectDeptList(param);
	}
}
