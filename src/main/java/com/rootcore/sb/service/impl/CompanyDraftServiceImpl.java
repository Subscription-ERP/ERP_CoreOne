package com.rootcore.sb.service.impl;

import com.rootcore.sb.mapper.CompanyMapper;
import com.rootcore.sb.service.CompanyDraftService;
import com.rootcore.sb.vo.CompanyVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CompanyDraftServiceImpl implements CompanyDraftService {

    private static final String SESSION_KEY = "company";
    private final CompanyMapper companyMapper;

    @Override
    public void saveDraft(CompanyVO requestVO, HttpSession session) {
        if (requestVO == null) {
            throw new IllegalArgumentException("회사 정보가 없습니다.");
        }

        // 1) 필수값 검사
        validateRequired(requestVO);

        validateFormat(requestVO);  // 형식 검사

        // 2) 값 정리(공백/하이픈 제거), 정규화
        normalize(requestVO);
        
        validateDuplicate(requestVO);  // 3) 중복 체크 (UX)
        
        // 3) 세션 저장 (여기서 저장하니까 컨트롤러에서 또 setAttribute 하면 중복)
        session.setAttribute(SESSION_KEY, requestVO);
    }

    @Override
    public CompanyVO getDraft(HttpSession session) {
        Object obj = session.getAttribute(SESSION_KEY);
        if (obj == null) {
            throw new IllegalStateException("세션이 만료되었거나 회사 정보가 없습니다.");
        }
        return (CompanyVO) obj;
    }

    @Override
    public void clearDraft(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
    }

    // 검증 필수값
    
    public class ValidationException extends RuntimeException {
        private final Map<String, String> fieldErrors;

        public ValidationException(Map<String, String> fieldErrors) {
            super("validation failed");
            this.fieldErrors = fieldErrors;
        }

        public Map<String, String> getFieldErrors() {
            return fieldErrors;
        }
    }
    private void validateRequired(CompanyVO requestVO) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (isBlank(requestVO.getCompanyName())) {
            errors.put("companyName", "회사명은 필수입니다.");
        }
        if (isBlank(requestVO.getBno())) {
            errors.put("bno", "사업자번호는 필수입니다.");
        }
        if (isBlank(requestVO.getCeoName())) {
            errors.put("ceoName", "대표자명은 필수입니다.");
        }
        if (isBlank(requestVO.getCeoPhone())) {
            errors.put("ceoPhone", "휴대폰번호는 필수입니다.");
        }
        if (isBlank(requestVO.getManagerEmail())) {
        	errors.put("managerEmail", "담당자이메일은 필수입니다.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void normalize(CompanyVO requestVO) {
        requestVO.setCompanyName(requestVO.getCompanyName().trim());
        requestVO.setCeoName(requestVO.getCeoName().trim());
        requestVO.setBno(requestVO.getBno().replace("-", "").trim());
        requestVO.setCeoPhone(requestVO.getCeoPhone().replace("-", "").trim());
        // 담당자 이메일 정규화
        requestVO.setManagerEmail(
            requestVO.getManagerEmail().trim().toLowerCase()
        );
    }
    
    private void validateFormat(CompanyVO requestVO) {
        Map<String, String> errors = new LinkedHashMap<>();

        // 사업자번호: 숫자 10자리
        if (!requestVO.getBno().matches("^\\d{10}$")) {
            errors.put("bno", "사업자번호는 '-' 없이 10자리 숫자여야 합니다.");
        }

        // 휴대폰번호: 01X로 시작, 10~11자리
        if (!requestVO.getCeoPhone().matches("^01\\d{8,9}$")) {
            errors.put("ceoPhone", "휴대폰번호는 '-' 없이 숫자만 입력해주세요.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    //중복값 체크
    private void validateDuplicate(CompanyVO vo) {
    	// normalize 이후에 검사해야 함(하이픈 제거된 값으로)
    	if (companyMapper.countByBno(vo.getBno()) > 0) {
    		Map<String, String> errors = new LinkedHashMap<>();
    		errors.put("bno", "이미 등록된 사업자번호입니다.");
    		throw new ValidationException(errors);
    	}
    }
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

