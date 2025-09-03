package org.adzc.elevenapi.config;

import org.adzc.elevenapi.auth.CurrentUidResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射 /uploads/** 到本地 ./uploads 目录（开发方便）
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUidResolver());
    }
}
