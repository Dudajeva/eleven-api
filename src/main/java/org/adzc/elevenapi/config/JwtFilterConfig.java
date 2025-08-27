package org.adzc.elevenapi.config;

import org.adzc.elevenapi.auth.JwtAuthFilter;
import org.adzc.elevenapi.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtFilterConfig {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-seconds}")
    private long jwtExpSeconds;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(jwtSecret, jwtExpSeconds);
    }

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtAuthFilterRegistration(JwtUtil jwtUtil) {
        FilterRegistrationBean<JwtAuthFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new JwtAuthFilter(jwtUtil));
        reg.addUrlPatterns("/api/*"); // 鉴权保护所有 /api/*，登录接口在过滤器里已放行
        reg.setOrder(1);
        return reg;
    }
}
