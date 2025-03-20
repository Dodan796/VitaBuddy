package com.example.vitabuddy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webConfig implements WebMvcConfigurer {

    // CORS 설정 (필요시)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해
                .allowedOrigins("http://localhost:3000")  // 허용할 도메인 설정
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    // 정적 리소스 매핑
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	  registry.addResourceHandler("/images/**")
          .addResourceLocations("file:///C:/supplement_images/"); //+로컬경로    	      	 
    	  //.addResourceLocations("file:///usr/local/project/supplement_images/");
    	  
    	  registry.addResourceHandler("/Review_Upload/**")
          .addResourceLocations("file:///C:/Review_Upload/"); //+로컬경로
    	  
    	  // 서버경로
    	  //.addResourceLocations("file:///usr/local/project/upload/");
    }
}