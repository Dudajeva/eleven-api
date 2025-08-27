package org.adzc.elevenapi.auth;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
@CrossOrigin(origins = "*") // 开发期放开，生产记得收紧
public class AuthController {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-seconds}")
    private long jwtExpSeconds;

    private final BCryptPasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public AuthController(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // ===== 内存演示用户（后续替换为 MyBatis/MySQL）=====
    private static class DemoUser {
        String id;
        String identity;      // 邮箱或电话（唯一）
        String passwordHash;  // BCrypt
        String nickname;
    }
    private final Map<String, DemoUser> users = new HashMap<>();

    @PostConstruct
    public void init() {
        this.jwtUtil = new JwtUtil(jwtSecret, jwtExpSeconds);

        // 统一用同一个 passwordEncoder 生成哈希，避免版本/实现不一致
        String demoHash = passwordEncoder.encode("12345678");

        DemoUser u1 = new DemoUser();
        u1.id = "u_10001";
        u1.identity = "demo@example.com";
        u1.passwordHash = demoHash;
        u1.nickname = "Demo";

        DemoUser u2 = new DemoUser();
        u2.id = "u_10002";
        u2.identity = "13800000000";
        u2.passwordHash = demoHash;
        u2.nickname = "小明";

        // 用规范化键保存（邮箱小写；手机去空格）
        users.put(normalizeIdentity(u1.identity), u1);
        users.put(normalizeIdentity(u2.identity), u2);
    }

    private String normalizeIdentity(String s) {
        if (s == null) return null;
        s = s.trim();
        // 简单判断：含 @ 视为邮箱 → 小写；否则视为电话 → 去空格
        if (s.contains("@")) return s.toLowerCase(Locale.ROOT);
        return s.replaceAll("\\s+", "");
    }

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
        String idNorm = normalizeIdentity(req.identity);
        DemoUser u = users.get(idNorm);
        if (u == null) {
            throw new BadRequestException("用户名或密码错误");
        }
        // 口令去前后空格再比对
        String raw = req.password == null ? "" : req.password.trim();
        if (!passwordEncoder.matches(raw, u.passwordHash)) {
            throw new BadRequestException("用户名或密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", u.id);
        claims.put("nick", u.nickname);

        String token = jwtUtil.generateToken(u.identity, claims);
        return new LoginResponse(token, u);
    }

    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    static class BadRequestException extends RuntimeException {
        public BadRequestException(String msg) { super(msg); }
    }
}
