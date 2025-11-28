package com.rootcore.hr.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.HrService;
import com.rootcore.hr.vo.DeptVO;
import com.rootcore.hr.vo.UserVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class HrRestController {
	
	private final HrService hrService;
	
	// 사원 전체조회
	@GetMapping("/userAllList")
	public List<UserVO> getUserAllList(){
		return hrService.selectAllUserList();
	}
	
	// 사원 상세조회
	@GetMapping("/userDetail")
	public UserVO getUserDetail(String userId) {
		return hrService.selectUserDetail(userId);
		// 요청: /api/hr/empDetail?userId=EMP23030100003
	}
	
	// 부서조회
	@GetMapping("/getDeptName")
	public List<DeptVO> getDeptName() {
		return hrService.selectDeptMaster();
	}
}
