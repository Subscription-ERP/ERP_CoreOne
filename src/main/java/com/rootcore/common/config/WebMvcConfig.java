package com.rootcore.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry; // 정적자원(이미지, css..) 매핑 설정용
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; //스프링MVC커스텀 인터페이스

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	// WebMvcConfig : 저장한 파일을 /upload/** URL로 브라우저에서 볼 수 있게 연결
	// -> 쉽게 말해서, 저장된 파일을 브라우저에서 어떻게 볼 건가?를 담당하는 설정
	
	@Value("${app.upload-dir:/upload}")
	private String uploadDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize(); // 절대경로로 변경
		String resourceLocation = uploadPath.toUri().toString(); 
		// toUri() -> file: 스킴이 포함된 URI 문자열 변환(마지막 /까지 포함)

		registry.addResourceHandler("/upload/**")
		        .addResourceLocations(resourceLocation)
		        .setCachePeriod(3600); // 브라우저 캐시 시간
	}
}
