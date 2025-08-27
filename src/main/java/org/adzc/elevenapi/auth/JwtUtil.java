package org.adzc.elevenapi.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 * 使用 HS256 对称签名
 * 生成时避免 setClaims 覆盖标准字段（例如 sub/iat/exp）
 */
public class JwtUtil {

    /**
     * 签名密钥
     */
    private final SecretKey key;

    /**
     * 过期时间（毫秒）
     */
    private final long expirationMillis;

    /**
     * 构造函数
     * secret 需足够长度以满足 HS256 要求
     * expirationSeconds 为令牌有效期（秒）
     */
    public JwtUtil(String secret, long expirationSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationSeconds * 1000;
    }

    /**
     * 生成 JWT
     * subject 建议放用户唯一标识（本项目用 identity：邮箱或手机号）
     * claims 为自定义声明（如 uid、nick 等）
     * 注意：不要使用 setClaims 以免覆盖标准字段
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        long now = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMillis));

        if (claims != null) {
            for (Map.Entry<String, Object> e : claims.entrySet()) {
                builder.claim(e.getKey(), e.getValue());
            }
        }

        return builder
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析并校验 JWT
     * 成功返回 Jws<Claims>，失败抛出 JwtException
     */
    public Jws<Claims> parseAndValidate(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
