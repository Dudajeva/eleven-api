package org.adzc.elevenapi.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
@CrossOrigin(origins = "*") // 开发期放开，生产收紧
public class AuthController {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-seconds}")
    private long jwtExpSeconds;

    private JwtUtil jwtUtil;
    private BCryptPasswordEncoder passwordEncoder;

    // 内存演示账号（将来切到 MySQL + MyBatis）
    private static class DemoUser {
        String id;
        String identity; // 邮箱或电话
        String passwordHash;
        String nickname;
    }
    private final Map<String, DemoUser> users = new HashMap<>();

    @PostConstruct
    public void init() {
        this.jwtUtil = new JwtUtil(jwtSecret, jwtExpSeconds);
        this.passwordEncoder = new BCryptPasswordEncoder();

        // 预置一个演示用户：identity= demo@example.com 或 13800000000
        DemoUser u1 = new DemoUser();
        u1.id = "u_10001";
        u1.identity = "demo@example.com";
        // 密码为 "12345678" 的 BCrypt 哈希（固定示例）
        u1.passwordHash = "$2a$10$kLk6w5b0yC3k3eF9g9m1qu8xq1mHqY4mG8o8TgQzqQi6y1qv1Q3rS";
        u1.nickname = "Demo";

        DemoUser u2 = new DemoUser();
        u2.id = "u_10002";
        u2.identity = "13800000000";
        u2.passwordHash = u1.passwordHash;
        u2.nickname = "小明";

        users.put(u1.identity, u1);
        users.put(u2.identity, u2);
    }

    // 请求/响应 DTO —— 简洁明了
    public static class LoginRequest {
        @NotBlank(message = "identity 不能为空（邮箱或电话）")
        public String identity;
        @NotBlank(message = "password 不能为空")
        public String password;
    }
    public static class LoginResponse {
        public String token;
        public String userId;
        public String identity;
        public String nickname;

        public LoginResponse(String token, DemoUser u) {
            this.token = token;
            this.userId = u.id;
            this.identity = u.identity;
            this.nickname = u.nickname;
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        DemoUser u = users.get(req.identity);
        if (u == null || !passwordEncoder.matches(req.password, u.passwordHash)) {
            // 与前端“密码错误”文案一致
            throw new BadRequestException("用户名或密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", u.id);
        claims.put("nick", u.nickname);

        String token = jwtUtil.generateToken(u.identity, claims);
        return new LoginResponse(token, u);
    }

    // 简单 400 异常
    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    static class BadRequestException extends RuntimeException {
        public BadRequestException(String msg) { super(msg); }
    }
}
