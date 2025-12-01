package com.rootcore.hr.web;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.hr.service.HrService;
import com.rootcore.hr.vo.DeptVO;
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
	
	// 사원카드PDF
	@GetMapping("/userCard")
	public ResponseEntity<byte[]> downloadSampleEmployeeCard(String userId) throws Exception {

        byte[] pdfBytes = hrService.userCardPdf(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // 파일 이름 한글이면 인코딩 따로 신경 써야 할 수 있음
        headers.setContentDisposition(ContentDisposition.inline()
        						                        .filename("userCard-" + userId + ".pdf", StandardCharsets.UTF_8)
        						                        .build()
        );
        //headers.setContentDispositionFormData("attachment", "userCard-" + userId + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

    }
}
