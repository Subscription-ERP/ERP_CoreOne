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

/**
 * 조직도 관리 페이지 컨트롤러입니다. 조직도 관리 페이지에서 필요한 부서조회(다건), 사원조회(다건)가 이루어집니다.
 * 
 * @author 장준현
 * @version 1.1.0
 * @since 2025-12-12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class DeptManageRestController {

	/**
	 * 부서 및 사원 관련 데이터베이스 접근을 담당하는 매퍼 객체
	 */
	private final DeptManageService deptManageService;

	/**
	 * HTTP 세션에서 로그인된 회사의 코드를 추출하여 파라미터 VO객체에 설정합니다.
	 * 
	 * @param param   회사 코드를 담을 VO객체
	 * @param session HTTP 세션
	 * @since 1.1.0
	 */
	private void setCompanyCode(DeptMasterVO param, HttpSession session) {
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		param.setCompanyCode(CompanyCode);
	}

	/**
	 * HTTP 세션에서 로그인된 사용자의 Id를 추출하여 파라미터 VO객체에 설정합니다.
	 * 
	 * @param param   사용자 Id를 담을 VO객체
	 * @param session HTTP 세션
	 * @since 1.1.0
	 */
	private void setUserId(DeptMasterVO param, HttpSession session) {
		String UserId = (String) session.getAttribute("LOGIN_USER_ID");
		param.setUserId(UserId);
	}

	/**
	 * TUI Grid의 API 응답 형식에 맞도록 데이터를 래핑하여 반환합니다. 이 응답은 반드시
	 * {@code {"result":true,"data":{"contents": [...]}}} 구조를 가집니다.
	 * 
	 * @param list 그리드에 표시될 실제 데이터 목록 (예: List<UserVO>, List<DeptMasterVO> 등)
	 * @return TUI Grid 데이터 읽기(read) 요청을 위한 표준 응답 Map.
	 * @since 1.1.0
	 */
	private Map<String, Object> buildTuiGridResponse(List<?> list) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);
		result.put("data", data);
		return result;
	}

	// 조직도관리-부서조회-tree버전
	@GetMapping("/deptStructure")
	public List<DeptMasterVO> selectDeptList(DeptMasterVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		param.setCompanyCode(CompanyCode);

		return deptManageService.selectDeptList(param);
	}

	/**
	 * 특정 부서에 속한 사원 목록을 조회합니다. (TUI Grid 데이터 형식) CompanyCode는 세션에서 자동으로 설정됩니다.
	 * 
	 * @param param   검색 조건(필터링 조건)을 담은 VO 객체
	 * @param session 회사 코드를 가져오기 위한 HTTP 세션
	 * @return TUI Grid의 readData 형식에 맞는 Map 객체. data.contents에 사원 목록(UserVO)이
	 *         포함됩니다.
	 * @since 1.1.0
	 */
	@GetMapping("/deptUserList")
	public Map<String, Object> selectDeptUserList(DeptMasterVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		setCompanyCode(param, session);

		// 부서코드가 넘어오지 않을 경우, 세션에서 로그인된 사용자의 부서코드를 가져와 설정
		if (param.getText() == null || param.getText().isEmpty()) {
			setUserId(param, session); // 사용자Id
			String deptName = deptManageService.findDeptNameByUserId(param); // 현재 접속한 회사코드와 사용자Id의 부서이름 불러오기
			param.setText(deptName); // 불러온 부서이름 param에 넣기
		}

		// 서비스를 통해서 사원데이터 가져오기
		List<UserVO> list = deptManageService.selectDeptUserList(param);

		// return으로는 TUI Grid readData형식에 맞춰서 반환
		return buildTuiGridResponse(list);
	}

	// 조직도관리-부서조회-grid버전
	@GetMapping("/deptStructureGrid")
	public Map<String, Object> selectDeptListGrid(DeptMasterVO param, HttpSession session) {
		// 세션에서 회사코드 들고오기
		String CompanyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		param.setCompanyCode(CompanyCode);

		List<DeptMasterVO> list = deptManageService.selectDeptList(param);

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
