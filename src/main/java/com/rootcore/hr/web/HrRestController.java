package com.rootcore.hr.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.rootcore.hr.service.HrService;
import com.rootcore.hr.vo.DeptVO;
import com.rootcore.hr.vo.UserHistoryVO;
import com.rootcore.hr.vo.UserSearchVO;
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
	
	// 사원 검색
	@GetMapping("/userSearch")
	public List<UserVO> searchUserList(UserSearchVO userSearchVO){
		return hrService.selectUserSearch(userSearchVO);
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
	
	
	// 사원카드PDF 미리보기
	@GetMapping("/userCard/preview")
	public ModelAndView userCardPreview(@RequestParam String userId) {
		
		// 데이터조회
		UserVO user = hrService.selectUserDetail(userId);
		if(user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					userId + "사원을 찾을 수 없습니다.");
		}
		
		// 템플릿에 넘길 data 맵 생성
		Map<String, Object> data = new HashMap<>();
		data.put("user", user);
		
		// pdfView로 ModelAndView 생성
		ModelAndView mav = new ModelAndView("pdfView");
		
		// pdfView에서 사용할 템플릿 이름(template/pdf/userCard.html)
		mav.addObject("templateName", "pdf/userCard");
		
		// 템플릿에 전달할 실제 데이터
		mav.addObject("data", data);
		
		// 미리보기
		mav.addObject("disposition", "inline");
				
		return mav;
		
	}	
	
	// 사원카드PDF 다운로드
	@GetMapping("/userCard/download")
	public ModelAndView userCardPDF(@RequestParam String userId) {
		
		// 데이터조회
		UserVO user = hrService.selectUserDetail(userId);
		if(user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					userId + "사원을 찾을 수 없습니다.");
		}
		
		// 템플릿에 넘길 data 맵 생성
		Map<String, Object> data = new HashMap<>();
		data.put("user", user);
		
		// pdfView로 ModelAndView 생성
		ModelAndView mav = new ModelAndView("pdfView");
		
		// pdfView에서 사용할 템플릿 이름(template/pdf/userCard.html)
		mav.addObject("templateName", "pdf/userCard");
		
		// 템플릿에 전달할 실제 데이터
		mav.addObject("data", data);
		
		// 파일명(옵션)
		mav.addObject("fileName", "userCard-" + user.getUserId() + ".pdf");
				
		return mav;
	}
	
	// 사원 이력 인쇄
	@GetMapping("/userHistory/preview")
	public ModelAndView userHistoryPreview(@RequestParam String userId) {
		
		// 데이터조회
		UserVO user = hrService.selectUserDetail(userId);
		if(user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					userId + "사원을 찾을 수 없습니다.");
		}
		
		// UserVO 안의 historyList를 타입별로 분리
		// historyList null 방지 + null 요소 제거
	    List<UserHistoryVO> historyList =
	            Optional.ofNullable(user.getHistoryList())
	                    .orElse(Collections.emptyList());

	    List<UserHistoryVO> deptHistoryList = historyList.stream()
	            .filter(Objects::nonNull)
	            .filter(h -> "f1".equals(h.getHistType()))
	            .toList();

	    List<UserHistoryVO> salaryHistoryList = historyList.stream()
	            .filter(Objects::nonNull)
	            .filter(h -> "f2".equals(h.getHistType()))
	            .toList();

	    // 템플릿에 내려줄 데이터 세팅
	    Map<String, Object> data = new HashMap<>();
	    data.put("user", user);
	    data.put("deptHistoryList", deptHistoryList);
	    data.put("salaryHistoryList", salaryHistoryList);

	    // pdfView에 넘기기
	    ModelAndView mav = new ModelAndView("pdfView");
	    mav.addObject("templateName", "pdf/userHistoryCard"); // html 경로
	    mav.addObject("data", data);
	    mav.addObject("fileName", "userHistory-" + user.getUserId() + ".pdf"); //파일옵션
	    mav.addObject("disposition", "inline"); // 브라우저에서 바로 열기

	    return mav;
	}
	
	
}
