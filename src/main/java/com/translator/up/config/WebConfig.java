package com.translator.up.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000") // Cho phép React app ở localhost:3000
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Các phương thức HTTP cho phép
                .allowCredentials(true); // Cho phép gửi cookie
    }
}
