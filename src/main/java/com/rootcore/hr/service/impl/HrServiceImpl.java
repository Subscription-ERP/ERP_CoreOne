package com.rootcore.hr.service.impl;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.rootcore.hr.mapper.HrMapper;
import com.rootcore.hr.service.HrService;
import com.rootcore.hr.vo.CertificationVO;
import com.rootcore.hr.vo.DeptVO;
import com.rootcore.hr.vo.UserHistoryVO;
import com.rootcore.hr.vo.UserSearchVO;
import com.rootcore.hr.vo.UserVO;
import com.rootcore.hr.vo.WorkExperienceVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HrServiceImpl implements HrService {

	private final HrMapper hrMapper;

	// 사원 	
	// 전체조회
	@Override
	public List<UserVO> selectAllUserList() {
		return hrMapper.selectAllUserList();
	}
	
	// 상세조회 - 기본사항/자격증/경력사항/이력
	@Override
	public UserVO selectUserDetail(String userId) {
		// 1) 사원 기본사항 조회
	    UserVO user = hrMapper.selectUserDetail(userId);
		
	    // 2) 없는 사원일 경우
        if (user == null) {
        	return null; 
        }
		
        // 3) 자격증/경력사항/이력 조회
        List<CertificationVO> certi = hrMapper.selectUserCertification(userId); 
        List<WorkExperienceVO> wex = hrMapper.selectUserWorkExperience(userId);
        List<UserHistoryVO> hist = hrMapper.selectUserHistory(userId);
        
		// 4) userVO안의 리스트필드에 세팅
        user.setCertificationList(certi);
        user.setWorkExperienceList(wex);
        user.setHistoryList(hist);
			
		return user;
	}
	
	// 부서조회
	@Override
	public List<DeptVO> selectDeptMaster() {
		return hrMapper.selectDeptMaster();
	}
	
	// 검색
	@Override
	public List<UserVO> selectUserSearch(UserSearchVO userSearchVO) {
		return hrMapper.selectUserSearch(userSearchVO);
	}
		
	
	// 사원카드PDF
	@Override
	public byte[] userCardPdf(String userId) throws Exception {

		// 상세조회
		UserVO user = selectUserDetail(userId);
		
		if(user == null) {
			throw new IllegalArgumentException(userId + " 해당 사원이 없습니다. ");
		}
		
		// 템플릿 HTML 읽기 (static/pdf/userCard.html)
        ClassPathResource resource = new ClassPathResource("static/pdf/userCard.html");
        Document doc = Jsoup.parse(resource.getInputStream(), "UTF-8", "");
        
        // 값 매핑
        // 기본정보
        doc.getElementById("headerEmpNo").text(user.getUserId());   
        doc.getElementById("empNo").text(user.getUserId());
        doc.getElementById("empName").text(user.getUserName());
        doc.getElementById("phone").text(nullToEmpty(user.getTel()));
        doc.getElementById("email").text(nullToEmpty(user.getEmail()));
        doc.getElementById("hireDate").text(nullToEmpty(formatDate(user.getHireDate())));
        doc.getElementById("hireTypeName").text(nullToEmpty(user.getHireTypeName()));  
        doc.getElementById("deptName").text(nullToEmpty(user.getDeptName()));  
        doc.getElementById("jobTitle").text(nullToEmpty(user.getJobTitleName()));
        doc.getElementById("position").text(nullToEmpty(user.getPositionName()));
        doc.getElementById("zipCode").text(nullToEmpty(user.getZipCode()));
        doc.getElementById("address").text(nullToEmpty(user.getAddress()));
        doc.getElementById("familyCnt").text(toStr(user.getFamilyCount()));
        doc.getElementById("childrenCnt").text(toStr(user.getChildrenCount()));
        doc.getElementById("householder").text(formatHouseholder(user.getHouseholder()));
        doc.getElementById("leaveDate").text(nullToEmpty(formatDate(user.getLeaveDate())));
        doc.getElementById("leaveReason").text(nullToEmpty(user.getLeaveReason()));
        
        // 급여
        doc.getElementById("bankName").text(nullToEmpty(user.getBankName()));
        doc.getElementById("accountNo").text(nullToEmpty(user.getAccountNo()));
        doc.getElementById("accountHolder").text(nullToEmpty(user.getAccountHolder()));
        doc.getElementById("salary").text(nullToEmpty(formatNumber(user.getSalary())));
        
        // 자격증
        Element licenseTbody = doc.getElementById("licenseList");
        licenseTbody.empty();      // 기존 샘플 행 제거

        List<CertificationVO> certList = user.getCertificationList();
        if (certList != null && !certList.isEmpty()) {
            for (CertificationVO c : certList) {
            	if(c == null) continue;  // null에러
            	
                licenseTbody.append(
                    "<tr>"
                        + "<td>" + safe(c.getCertiName()) + "</td>"
                        + "<td>" + safe(c.getIssueOrgName()) + "</td>"
                        + "<td>" + safe(formatDate(c.getGetDate())) + "</td>"
                        + "<td>" + safe(c.getLicenseNo()) + "</td>"
                        + "<td>" + safe(formatDate(c.getExpireDate())) + "</td>"
                        + "<td>" + safe(c.getRemark()) + "</td>"
                    + "</tr>"
                );
            }
        }
        
        // 경력사항
        Element careerTbody = doc.getElementById("careerList");
        careerTbody.empty();

        List<WorkExperienceVO> wexList = user.getWorkExperienceList();
        if (wexList != null && !wexList.isEmpty()) {
            for (WorkExperienceVO w : wexList) {
            	if(w == null) continue; // null 에러
            	
                careerTbody.append(
                    "<tr>"
                        + "<td>" + safe(w.getWexCompanyName()) + "</td>"
                        + "<td>" + safe(w.getWexDept()) + "</td>"
                        + "<td>" + safe(w.getWexJobTitle()) + "</td>"
                        + "<td>" + safe(formatDate(w.getWexHireDate())) + "</td>"
                        + "<td>" + safe(formatDate(w.getWexLeaveDate())) + "</td>"
                        + "<td>" + safe(w.getWexMainDuty()) + "</td>"
                        + "<td>" + safe(formatNumber(w.getWexSalary())) + "</td>"
                    + "</tr>"
                );
            }
        }
        
        // PDF 생성
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();

        // 폰트 
        builder.useFont(
                new ClassPathResource("static/font/malgun.ttf").getFile(),
                "Malgun Gothic"
        );
        
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(doc);

        builder.withW3cDocument(w3cDoc, "/");
        builder.toStream(baos);
        builder.run();

        return baos.toByteArray();
	}
	
	
	// null 방지용 유틸 메서드들 (HrServiceImpl 안에 private로 추가)
	private String nullToEmpty(String s) {
	    return s == null ? "" : s;
	}
	private String toStr(Object o) {
	    return o == null ? "" : String.valueOf(o);
	}
	private String safe(String s) {
	    return s == null ? "" : s.replace("<","&lt;").replace(">","&gt;");
	}
	
	// 날짜포맷
	private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");
	private String formatDate(Date date) {
	    if (date == null) return "";
	    return DATE_FMT.format(date);
	}
	
	// 연봉 
	private String formatNumber(Number n) {
	    if (n == null) return "";
	    return String.format("%,d", n.longValue());
	}
	
	// 세대주 여부 표시용
	private String formatHouseholder(String householder) {
	    if ("Y".equalsIgnoreCase(householder)) {
	        return "Y";        
	    }
	    return "해당사항없음";         
	}




	
	
	


}
