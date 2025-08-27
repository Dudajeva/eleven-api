package org.adzc.elevenapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class CryptoConfig {

    // 单例的 BCrypt 加密器，后续注册/修改密码也复用它
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 默认强度 10，足够；需要更强可调大代价
        return new BCryptPasswordEncoder();
    }
}
