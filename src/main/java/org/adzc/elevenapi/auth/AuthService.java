package org.adzc.elevenapi.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.adzc.elevenapi.domain.User;
import org.adzc.elevenapi.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 登录鉴权服务：使用 MyBatis 查库 + BCrypt 校验
 */
@Service
public class AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;

    public AuthService(UserMapper userMapper, JwtUtil jwtUtil, BCryptPasswordEncoder encoder) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    /** 登录结果：token + user */
    public static class LoginResult {
        private final String token;
        private final User user;
        public LoginResult(String token, User user) {
            this.token = token;
            this.user = user;
        }
        public String getToken() { return token; }
        public User getUser() { return user; }
    }

    /** 登录：identity（邮箱或手机号），password（明文） */
    public LoginResult login(String identity, String rawPassword) {
        String norm = normalizeIdentity(identity);
        User user = userMapper.findByIdentity(norm);
        if (user == null || !encoder.matches(safe(rawPassword), user.getPasswordHash())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        claims.put("nick", user.getNickname());
        // 如需：claims.put("tier", user.getTier());

        String subject = user.getIdentity() != null ? user.getIdentity() : norm;
        String token = jwtUtil.generateToken(subject, claims);
        return new LoginResult(token, user);
    }

    /** 解析 token */
    public Jws<Claims> parse(String token) {
        return jwtUtil.parseAndValidate(token);
    }

    /* ===== 工具方法 ===== */
    private static String normalizeIdentity(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.contains("@")) return s.toLowerCase(Locale.ROOT);
        return s.replaceAll("\\s+", "");
    }
    private static String safe(String s) { return s == null ? "" : s.trim(); }
}
