package com.rootcore.common.view;

import java.util.Map;

import org.springframework.web.servlet.view.AbstractView;

import com.rootcore.common.service.CommonService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PdfView extends AbstractView {

	final CommonService commonService;
	
	public PdfView(CommonService commonService) {
        this.commonService = commonService;
        setContentType("application/pdf");
    }
	
	@Override
    protected void renderMergedOutputModel(
            Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // 1) 템플릿 이름 + 데이터 꺼내기
        String templateName = (String) model.get("templateName");

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) model.get("data");

        // 파일명 옵션
        String fileName = (String) model.getOrDefault("fileName", templateName + ".pdf");

        if (templateName == null || data == null) {
            throw new IllegalArgumentException("Model must contain 'templateName' and 'data'");
        }

        // 2) Thymeleaf 템플릿을 HTML로 렌더링
        String html = commonService.renderHtmlTemplate(templateName, data);

        // 3) HTML → PDF 변환
        byte[] pdfBytes = commonService.generatePdfFromHTML(html);

        // 4) 응답 헤더 설정
        String disposition = (String) model.getOrDefault("disposition", "attachment");
        
        response.setContentType(getContentType());
        response.setHeader(
        		"Content-Disposition",
                disposition + "; filename=\"" + fileName + "\"");
        response.setContentLength(pdfBytes.length);

        // 5) PDF 바이트 출력
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }

}
