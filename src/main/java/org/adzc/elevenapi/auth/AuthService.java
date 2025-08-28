package org.adzc.elevenapi.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.adzc.elevenapi.domain.User;
import org.adzc.elevenapi.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录鉴权服务：使用 MyBatis 查库 + BCrypt 校验
 */
@Service
public class AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 登录：identity（邮箱或手机号），password（明文）
     */
    public String login(String identity, String password) {
        User user = userMapper.findByIdentity(identity);
        if (user == null) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        claims.put("nick", user.getNickname());
        claims.put("tier", user.getTier());

        // subject = identity（邮箱或手机号）
        return jwtUtil.generateToken(user.getIdentity(), claims);
    }

    /**
     * 解析 token，返回 Jws<Claims>
     */
    public Jws<Claims> parse(String token) {
        return jwtUtil.parseAndValidate(token);
    }
}
