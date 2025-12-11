package com.rootcore.hr.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.HrDocumentService;
import com.rootcore.hr.vo.UserVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class HrDocumentRestController {

	final private HrDocumentService hrDocumentService;
	
	// 사원조회 및 검색
	@GetMapping("/user/search")
	public List<UserVO> getUser(String userName, String userId){
		return hrDocumentService.selectUser(userName, userId);
	}
	
	
	
	
	
	
	
	
	
	
}
