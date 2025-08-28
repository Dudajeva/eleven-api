package org.adzc.elevenapi.bootstrap;

import org.adzc.elevenapi.domain.User;
import org.adzc.elevenapi.mapper.UserMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 启动时如果 users 表为空，自动插入一个演示账号：
 * identity: demo@example.com（也可用 13800000000 登录）
 * password: 123456
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public DataInitializer(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void run(ApplicationArguments args) {
        long cnt = userMapper.countUsers();
        if (cnt > 0) return;

        User u = new User();
        u.setIdentity("demo@example.com");
        u.setEmail("demo@example.com");
        u.setPhone("13800000000");
        u.setPasswordHash(encoder.encode("123456"));  // 运行时生成 BCrypt
        u.setNickname("Demo");
        u.setAge(28);
        u.setCity("上海");
        u.setTier("diamond");
        u.setPhotoUrl(null);

        userMapper.insertUser(u);
        System.out.println("[DataInitializer] inserted demo user: demo@example.com / 123456");
    }
}
