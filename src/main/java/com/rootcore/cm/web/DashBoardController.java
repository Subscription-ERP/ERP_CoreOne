package com.rootcore.cm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value; // 1. 중요: Lombok의 @Value와 혼동 주의
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rootcore.cm.service.DashBoardService;
import com.rootcore.hr.vo.AnnualLeaveDetailVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * 메인대쉬보드 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cm")
public class DashBoardController {
	
	private final DashBoardService dashBoardService;
	
	/**
	 * 오늘날짜 휴가 쓴 사람 불러오는거
	 */
	@GetMapping("/DashBoardAnnualLeaveDetail")
	public Map<String, Object> getDashBoardAnnualLeaveDetail(AnnualLeaveDetailVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		param.setCompanyCode(CompanyCode);

		// 사원급여조회
		List<AnnualLeaveDetailVO> list = dashBoardService.selectDashBoardAnnualLeaveDetail(param);

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

	// 2. Lombok의 @Value가 아니라 Spring의 @Value여야 합니다.
	@Value("${weather.api.key}")
	private String apiKey;

	@GetMapping("/weather") // 3. 경로 수정: 클래스 상단에 /api/cm이 있으므로 여기는 /weather만 적습니다.
	public Object getProxyWeather(@RequestParam(defaultValue = "Daegu") String city) {
		// 서버 콘솔에 키가 출력되는지 확인해보세요. 만약 null이 나오면 Import 문제입니다.
	    System.out.println("로드된 API 키: " + apiKey);

		String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=kr",
				city, apiKey);

		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, Object.class);
	}
}