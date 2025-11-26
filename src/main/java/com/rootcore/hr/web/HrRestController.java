package com.rootcore.hr.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.HrService;
import com.rootcore.hr.vo.UserVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class HrRestController {
	private final HrService hrService;
	
	// 급여대장-상여등록-사원조회
	@GetMapping("/emplist")
	public List<UserVO> getUserList(UserVO param){
		return hrService.selectUserList(param);
	}
	
	@GetMapping("/empAllList")
	public List<UserVO> getUserAllList(){
		return hrService.selectAllUserList();
	}
	
	
	
}
