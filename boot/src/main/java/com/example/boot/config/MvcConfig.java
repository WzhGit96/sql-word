package com.example.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;

/**
 * @author wzh
 * @since 2021/4/26
 */
@Configuration
public class MvcConfig {
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                try {
                    String rootPath = ResourceUtils.getURL("/").getPath();
                    registry.addResourceHandler(rootPath + File.separator + "static/**");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
