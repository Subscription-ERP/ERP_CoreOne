package com.rootcore.auth.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rootcore.auth.service.FindIdService;
import com.rootcore.auth.vo.LoginFindIdVO;

	@Controller
	@RequestMapping("/auth")
	public class FindIdController {

	    @Autowired
	    private FindIdService findIdService;

	    // 아이디 찾기 화면
	    @GetMapping("/find_id")
	    public String findIdPage() {
	        return "auth/find_id"; // templates/auth/find-id.html
	    }

	    // 아이디 찾기 처리 (POST)
	    @PostMapping("/find_id")
	    public String findIdResult(@RequestParam("email") String email, Model model) {

	        List<LoginFindIdVO> resultList = findIdService.findIdByEmail(email);

	        model.addAttribute("resultList", resultList);
	        model.addAttribute("email", email);

	        return "auth/find_id"; // 같은 화면에서 결과 표시
	    }


  }

