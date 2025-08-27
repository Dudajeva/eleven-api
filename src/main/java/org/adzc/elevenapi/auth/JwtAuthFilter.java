package org.adzc.elevenapi.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.http.MediaType;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper om = new ObjectMapper();

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 放行登录接口
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/")) {
            chain.doFilter(req, res);
            return;
        }

        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            unauthorized(response, "缺少 Authorization Bearer 令牌");
            return;
        }
        String token = auth.substring(7).trim();
        try {
            Jws<Claims> jws = jwtUtil.parseAndValidate(token);
            Claims c = jws.getBody();
            // 将 token 信息塞到请求属性，方便控制器读取
            request.setAttribute("auth.identity", jws.getBody().getSubject());
            request.setAttribute("auth.uid", c.get("uid"));
            request.setAttribute("auth.nick", c.get("nick"));
            chain.doFilter(req, res);
        } catch (JwtException e) {
            unauthorized(response, "无效或过期的令牌");
        }
    }

    private void unauthorized(HttpServletResponse resp, String msg) throws IOException {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        om.writeValue(resp.getWriter(), java.util.Map.of(
                "status", 401,
                "error", "Unauthorized",
                "message", msg
        ));
    }
}
