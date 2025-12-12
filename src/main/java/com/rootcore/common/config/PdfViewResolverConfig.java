package com.rootcore.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import com.rootcore.common.service.CommonService;
import com.rootcore.common.view.JasperPdfView;
import com.rootcore.common.view.PdfView;

@Configuration
public class PdfViewResolverConfig {

    private final CommonService commonService;

    public PdfViewResolverConfig(CommonService commonService) {
        this.commonService = commonService;
    }

    // ★ 1) BeanNameViewResolver 등록 (이름 안 겹치게)
    @Bean
    public ViewResolver pdfBeanNameViewResolver() {
        BeanNameViewResolver resolver = new BeanNameViewResolver();
        // ThymeleafViewResolver보다 먼저 실행되도록 우선순위 높게
        resolver.setOrder(Ordered.HIGHEST_PRECEDENCE); // 또는 0 정도로 주셔도 됩니다
        return resolver;
    }

    // ★ 2) View 이름 "pdfView"에 대응하는 View 빈
    @Bean(name = "pdfView")
    public View pdfView() {
        return new PdfView(commonService);
    }
    
    @Bean(name = "jasperPdfView")
    public View jasperpdfView() {
    	return new JasperPdfView(commonService);
    }
	
}
