package com.rootcore.common.view;

import java.util.Map;

import org.springframework.web.servlet.view.AbstractView;

import com.rootcore.common.service.CommonService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JasperPdfView extends AbstractView {

	private final CommonService commonService;

    public JasperPdfView(CommonService commonService) {
    	this.commonService = commonService;
        setContentType("application/pdf");
    }

    @Override
    protected void renderMergedOutputModel(
            Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String reportName = (String) model.get("reportName");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) model.get("params");

        String fileName = (String) model.getOrDefault("fileName", reportName + ".pdf");
        String disposition = (String) model.getOrDefault("disposition", "inline");
        
        byte[] pdfBytes = commonService.generatePdfFromJasper(reportName, params);

        response.setContentType(getContentType());
        response.setHeader("Content-Disposition", disposition + "; filename=\"" + fileName + "\"");
        response.setContentLength(pdfBytes.length);
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }
    
}
