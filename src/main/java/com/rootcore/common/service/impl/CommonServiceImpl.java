package com.rootcore.common.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.rootcore.common.mapper.CommonMapper;
import com.rootcore.common.service.CommonService;
import com.rootcore.common.vo.CommonVO;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service("commonService")
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

	final CommonMapper commonMapper;
	final SpringTemplateEngine templateEngine;
	final DataSource datasource;
	
	// 공통코드
	@Override
	public List<CommonVO> selectType(String groupCode) {
		return commonMapper.selectType(groupCode);
	}

	@Override
	public List<CommonVO> selectCode(String common) {
		return commonMapper.selectCode(common);
	}

	@Override
	public Map<String, List<CommonVO>> selectCodes(String... common) {
		Map<String, List<CommonVO>> map = new HashMap<String, List<CommonVO>>();
		for(String gpCd : common) {
			map.put(gpCd, commonMapper.selectCode(gpCd));
		}
		return map;
	}

	// PDF
	@Override
	public String renderHtmlTemplate(String templateName, Map<String, Object> data) {
		Context context = new Context();
		context.setVariables(data);
		return templateEngine.process(templateName, context);
	}

	@Override
	public byte[] generatePdfFromHTML(String htmlContent) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		PdfRendererBuilder builder = new PdfRendererBuilder();

		// 폰트설정
		builder.useFont(
				new ClassPathResource("font/malgun.ttf").getFile(),
				"Malgun Gothic"
		);

		// base URI
		builder.withHtmlContent(htmlContent, null);
		builder.toStream(os);
		builder.run();

		return os.toByteArray();
	}

	// JasperReports PDF 
	@Override
	public byte[] generatePdfFromJasper(String reportName, Map<String, Object> params) throws Exception {
		
		Connection conn = datasource.getConnection();
		
		// jrxml 또는 jasper 파일로드
		InputStream is = getClass().getResourceAsStream("/jasper/" + reportName + ".jasper");
		JasperReport report = (JasperReport) JRLoader.loadObject(is);
		
		// 채우기
        JasperPrint print = JasperFillManager.fillReport(report, params, conn);
		
		// PDF 반환		
		return JasperExportManager.exportReportToPdf(print);
	}

}






