package com.esquad.esquadbe.user.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);

    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("forward:/index.html");
        registry.addViewController("/join").setViewName("forward:/index.html");
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
