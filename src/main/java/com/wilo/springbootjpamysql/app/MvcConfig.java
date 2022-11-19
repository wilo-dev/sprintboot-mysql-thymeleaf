package com.wilo.springbootjpamysql.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    private final Logger log = LoggerFactory.getLogger(getClass());

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        WebMvcConfigurer.super.addResourceHandlers(registry);
//
//        // path relativo (modo literal)
////        registry.addResourceHandler("/uploads/**")
////                .addResourceLocations("file:/E:/Temp/uploads/");
//
//        //path absoluto (modo dinamico)
//        String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
//        log.info("resourcePath " + resourcePath);
//
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations(resourcePath);
//
//    }
}
