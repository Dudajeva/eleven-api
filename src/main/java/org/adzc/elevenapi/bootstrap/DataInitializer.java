package org.adzc.elevenapi.bootstrap;

import org.adzc.elevenapi.domain.User;
import org.adzc.elevenapi.mapper.UserMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * 若 users 表为空，插入一批演示数据（密码统一 123456）
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final Random random = new Random();

    public DataInitializer(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userMapper.countUsers() > 0) return;

        List<String> cities = List.of("上海","北京","深圳","广州","杭州","成都");
        List<String> tiers  = List.of("normal","diamond","supreme");

        for (int i = 1; i <= 30; i++) {
            User u = new User();
            String email = "user" + i + "@example.com";
            u.setIdentity(email);
            u.setEmail(email);
            u.setPhone("1390000" + String.format("%04d", i));
            u.setPasswordHash(encoder.encode("123456"));
            u.setNickname("我是用户名");
            u.setAge(20 + random.nextInt(15));
            u.setCity(cities.get(random.nextInt(cities.size())));
            u.setTier(tiers.get(random.nextInt(tiers.size())));
            u.setPhotoUrl("https://www.cpophome.com/wp-content/uploads/2020/11/Zhao-Liying.jpg"); // 先空，前端用粉色占位
            userMapper.insertUser(u);
        }

        System.out.println("[DataInitializer] inserted 30 demo users, password=123456");
    }
}
