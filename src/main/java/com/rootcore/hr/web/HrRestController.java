package com.rootcore.hr.web;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.rootcore.hr.service.HrService;
import com.rootcore.hr.vo.DeptVO;
import com.rootcore.hr.vo.UserHistoryVO;
import com.rootcore.hr.vo.UserSearchVO;
import com.rootcore.hr.vo.UserVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hr")
public class HrRestController {
	
	private final HrService hrService;
	
	/*
		GET  /api/hr/user             전체조회,검색
		GET  /api/hr/user/{userId}    상세조회
		POST /api/hr/user             등록
		PUT  /api/hr/user/{userId}    수정
		DELETE /api/hr/user/{userId}  삭제
		
		GET /api/hr/user/{userId}/card/preview      사원카드 미리보기
		GET /api/hr/user/{userId}/card/download     사원카드 다운로드
		GET /api/hr/user/{userId}/history/preview   이력 미리보기
	 */
	
	// 사원 전체조회 + 검색
	@GetMapping("/user")
	public List<UserVO> getUserAllList(UserSearchVO userSearchVO, HttpSession session){
		
		// 세션 회사코드
		String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		
		if(companyCode == null || companyCode.isBlank()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
	                "회사 정보가 없습니다. 로그인 상태를 확인하세요.");
		}
		
		// companyCode를 searchVO에 세팅
		userSearchVO.setCompanyCode(companyCode);
				
		return hrService.selectUserList(userSearchVO);
	}
	
	// 사원 상세조회(단건조회)
	@GetMapping("/user/{userId}")
	public UserVO getUserDetail(@PathVariable String userId) {
		return hrService.selectUserDetail(userId);
	}
	
	// 부서조회
	@GetMapping("/getDeptName")
	public List<DeptVO> getDeptName() {
		return hrService.selectDeptMaster();
	}
	
	// application.properties 설정값 변수에 주입해주는 어노테이션
	@Value("${app.upload-dir}")
	private String uploadDir;
	
	// 사원카드PDF 미리보기
	@GetMapping("/user/{userId}/card/preview")
	public ModelAndView userCardPreview(@PathVariable String userId) {
		
		// 데이터조회
		UserVO user = hrService.selectUserDetail(userId);
		if(user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					userId + "사원을 찾을 수 없습니다.");
		}
		
		// pdf 전용 사진 uri
		String photoUri = null;
		if(user.getUserPhoto() != null && !user.getUserPhoto().isBlank()) {
			
			// 실제 파일 절대경로
			Path uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
			// user.getUserPhoto() 값이 예: "user/photo/xxx.jpg" 라고 가정
			Path photoPath = uploadRoot.resolve(user.getUserPhoto()).normalize();
			
			if (Files.exists(photoPath)) {
	            photoUri = photoPath.toUri().toString(); // file:/D:/git/.../upload/user/photo/xxx.jpg
	        }
		}
		
		// 템플릿에 넘길 data 맵 생성
		Map<String, Object> data = new HashMap<>();
		data.put("user", user);
		data.put("photoUri", photoUri);
	
		ModelAndView mav = new ModelAndView("pdfView"); // pdfView로 ModelAndView 생성
		mav.addObject("templateName", "pdf/userCard");  // pdfView에서 사용할 템플릿 이름(template/pdf/userCard.html)
		mav.addObject("data", data);		            // 템플릿에 전달할 실제 데이터
		mav.addObject("disposition", "inline"); 		// 미리보기
				
		return mav;
		
	}	
	
	// 사원카드PDF 다운로드
	@GetMapping("/user/{userId}/card/download")
	public ModelAndView userCardPDF(@PathVariable String userId) {
		
		// 데이터조회
		UserVO user = hrService.selectUserDetail(userId);
		if(user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					userId + "사원을 찾을 수 없습니다.");
		}
		
		// 템플릿에 넘길 data 맵 생성
		Map<String, Object> data = new HashMap<>();
		data.put("user", user);
		
		ModelAndView mav = new ModelAndView("pdfView"); // pdfView로 ModelAndView 생성
		mav.addObject("templateName", "pdf/userCard");  // pdfView에서 사용할 템플릿 이름(template/pdf/userCard.html)
		mav.addObject("data", data); 		            // 템플릿에 전달할 실제 데이터
		mav.addObject("fileName", "userCard-" + user.getUserId() + ".pdf"); // 파일명(옵션)
				
		return mav;
	}
	
	// 사원 이력 인쇄
	@GetMapping("/user/{userId}/history/preview")
	public ModelAndView userHistoryPreview(@PathVariable String userId) {
		
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
	
	
	// 사원등록
	@PostMapping("/user")
	public ResponseEntity<?> registerUser(@RequestPart("user") UserVO userVO,
			HttpSession session,
			@RequestPart(value = "userPhoto", required = false) MultipartFile userPhoto,
			@RequestPart(value = "userFile", required = false) MultipartFile userFile,
			@RequestPart(value = "certiFiles", required = false) List<MultipartFile> certiFiles
			) throws Exception {
		
		// session에서 회사코드 및 User 가져오기
		String userId = (String) session.getAttribute("LOGIN_USER_ID");
		String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		if(companyCode == null || companyCode.isBlank()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
	                "회사 정보가 없습니다. 로그인 상태를 확인하세요.");
		}
		if(userId == null || userId.isBlank()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
	                "로그인된 유저 정보가 없습니다. 로그인 상태를 확인하세요.");
		}
		
		// userVO에 세팅
		userVO.setCompanyCode(companyCode);
		userVO.setCreatedBy(userId);		
		
		try {
			int result = hrService.insertUser(userVO, userPhoto, userFile, certiFiles);
			if(result == 1) {
				Map<String, Object> body = new HashMap<>();
				
				body.put("result", "success");
				body.put("message", "사원이 등록되었습니다. \n초기비밀번호 설정 이메일이 발송되었으니 확인해주세요.");
				
				return ResponseEntity.ok(body);
			}else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						             .body(Map.of("result", "fail", "message", "등록처리에 실패했습니다."));
			}				
		}catch(RuntimeException e) {
			// 비밀번호 재설정 이메일 발송 실패
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					             .body(Map.of("result", "fail", "message", e.getMessage()));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					             .body(Map.of("result", "fail", "message", "서버오류가 발생했습니다."));
		}
	}
	
	// 사원수정
	@PutMapping("/user/{userId}")
	public ResponseEntity<?> modifyUser(@PathVariable String userId,
			@RequestPart("user") UserVO userVO,
			@RequestPart(value = "userPhoto", required = false) MultipartFile userPhoto,
			@RequestPart(value = "userFile", required = false) MultipartFile userFile,
			@RequestPart(value = "certiFiles", required = false) List<MultipartFile> certiFiles,
			HttpSession session
			) throws Exception {
		
		// url의 userId와 body userId 일치시키기
		userVO.setUserId(userId);
		
		// session 정보 세팅
		userVO.setCompanyCode((String) session.getAttribute("LOGIN_COMPANY_CODE"));
		userVO.setUpdatedBy((String) session.getAttribute("LOGIN_USER_ID"));
		
		int result = hrService.updateUser(userVO, userPhoto, userFile, certiFiles);
		
		if(result == 1) {
			return ResponseEntity.ok(Map.of("result","success","message","수정되었습니다."));
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		             .body(Map.of("result", "fail", "message", "서버오류가 발생했습니다."));
		}
		
	}

	
	// 비밀번호 초기화 이메일 재전송
	@PostMapping("/user/{userId}/password/reset")
	public ResponseEntity<?> resendResetPasswordMail(@PathVariable String userId, HttpSession session) {

	    String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
	    String loginUserId = (String) session.getAttribute("LOGIN_USER_ID");

	    if (companyCode == null || companyCode.isBlank()) {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회사 정보가 없습니다.");
	    }
	    if (loginUserId == null || loginUserId.isBlank()) {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 정보가 없습니다.");
	    }

	    hrService.resendResetLink(companyCode, userId, loginUserId);

	    return ResponseEntity.ok(Map.of(
	        "result", "success",
	        "message", "초기 비밀번호 설정 이메일을 재전송했습니다."
	    ));
	}


	
	
}
