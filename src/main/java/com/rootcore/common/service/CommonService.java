package com.rootcore.common.service;

import java.util.List;
import java.util.Map;

import com.rootcore.common.vo.CommonVO;

public interface CommonService {
    List<CommonVO> selectCode(String common);                         //공통코드
    Map<String, List<CommonVO>> selectCodes(String ... common);       //공통코드
    List<CommonVO> selectType(String groupCode);                      // 거래처유형
    List<CommonVO> selectAttributeCode(String groupCode, String attribute);                      // 거래처유형

    // PDF - thymeleaf 템플릿을 HTML 문자열로 렌더링
    String renderHtmlTemplate(String templateName, Map<String, Object> data);
    // PDF - HTML 문자열을 PDF byte[]로 변환
    byte[] generatePdfFromHTML(String htmlContent) throws Exception;

    // JasperReport PDF
    byte[] generatePdfFromJasper(String reportName, Map<String, Object> params) throws Exception;
 
}