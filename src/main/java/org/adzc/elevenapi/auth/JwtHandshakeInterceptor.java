package org.adzc.elevenapi.message.ws;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.adzc.elevenapi.auth.JwtUtil; // 复用现有
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/** WebSocket 握手时用 JwtUtil 校验 JWT，并把 uid/identity 放到 session attributes */
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    public JwtHandshakeInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        var params = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        String token = params.getFirst("token");
        if (token == null) {
            var proto = request.getHeaders().getFirst("Sec-WebSocket-Protocol");
            if (proto != null && proto.startsWith("Bearer ")) token = proto.substring(7);
        }
        Jws<Claims> jws = jwtUtil.parseAndValidate(token); // 来自现有 JwtUtil
        Claims c = jws.getBody();
        attributes.put("uid", c.get("uid"));               // 你的 JwtAuthFilter 里同样塞了 uid/sub 等  :contentReference[oaicite:2]{index=2}
        attributes.put("identity", c.getSubject());        // sub = identity（手机号/邮箱）  :contentReference[oaicite:3]{index=3}
        return true;
    }

    @Override public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                         WebSocketHandler wsHandler, Exception exception) { }
}
