package org.adzc.elevenapi.message;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWsHandler chatWsHandler;
    private final org.adzc.elevenapi.message.ws.JwtHandshakeInterceptor jwtHandshakeInterceptor;

    public WebSocketConfig(ChatWsHandler chatWsHandler, org.adzc.elevenapi.message.ws.JwtHandshakeInterceptor jwtHandshakeInterceptor) {
        this.chatWsHandler = chatWsHandler;
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWsHandler, "/ws/chat")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOrigins("*"); // 生产建议改白名单
    }
}
