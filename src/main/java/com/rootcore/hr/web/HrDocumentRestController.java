package com.rootcore.hr.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.rootcore.hr.service.HrDocumentService;
import com.rootcore.hr.vo.HrDocumentVO;
import com.rootcore.hr.vo.UserVO;

import jakarta.servlet.http.HttpSession;
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
	
	// 증명서 등록
	@PostMapping("/docs")
	public ResponseEntity<?> makeDocument(@RequestBody HrDocumentVO hrDocumentVO, HttpSession session) {
		
		String userId = (String) session.getAttribute("LOGIN_USER_ID");
		String companyCode = (String) session.getAttribute("LOGIN_COMPANY_CODE");
		
		if (userId == null || companyCode == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body("로그인이 필요합니다.");
	    }
		
		// session 공통 값 세팅
		hrDocumentVO.setCompanyCode(companyCode);
		hrDocumentVO.setCreatedBy(userId);
		
		int result = hrDocumentService.insertHrDocument(hrDocumentVO);
		
		if(result > 0) {
			return ResponseEntity.status(HttpStatus.CREATED)
					             .body(hrDocumentVO);
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				             .body("문서 생성 실패");
	}
	
	
	// Jasper Controller  
	// i1 재직, i2 경력
	@GetMapping("/docs/{docCode}/preview")
	public ModelAndView previewPdfFromJasper(@PathVariable String docCode,HttpSession session) {
		
		HrDocumentVO doc = hrDocumentService.selectDocumentByCode(docCode);
		
		String reportName;
		
		if ("i1".equalsIgnoreCase(doc.getDocType())) {
		    reportName = "empCerti";   // 재직
		} else if ("i2".equalsIgnoreCase(doc.getDocType())) {
		    reportName = "carrerCerti";  // 경력
		} else {
		    throw new IllegalStateException("알 수 없는 docType=" + doc.getDocType());
		}
				
		Map<String, Object> params = new HashMap<>();
		params.put("docCode", docCode); 
		
		Map<String, Object> model = new HashMap<>();
		model.put("reportName", reportName);      // 템플릿 : /jasper/empCerti.jasper
		model.put("params", params);
		model.put("fileName", reportName + "_" + docCode + ".pdf");
		model.put("disposition", "inline");       // iframe 미리보기
		
		System.out.println("docCode=" + docCode + ", docType=" + doc.getDocType());
		return new ModelAndView("jasperPdfView", model);
	}
	
	
	
	
}
