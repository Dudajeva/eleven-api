package org.adzc.elevenapi.message.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.adzc.elevenapi.message.service.ChatService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/** 单聊 WS：多端在线、ACK、未读更新、已读回执（最小可用版） */
@Component
public class ChatWsHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final ObjectMapper om = new ObjectMapper();

    // 一个用户 -> 多个连接
    private final ConcurrentHashMap<Long, CopyOnWriteArraySet<WebSocketSession>> userSessions = new ConcurrentHashMap<>();

    public ChatWsHandler(ChatService chatService) { this.chatService = chatService; }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long uid = (Long) session.getAttributes().get("uid");
        userSessions.computeIfAbsent(uid, k -> new CopyOnWriteArraySet<>()).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long me = (Long) session.getAttributes().get("uid");
        JsonNode root = om.readTree(message.getPayload());
        String type = root.path("type").asText();

        switch (type) {
            case "send" -> handleSend(me, root);
            case "read" -> handleRead(me, root);
            case "typing" -> handleTyping(me, root);
            default -> sendToUser(me, "{\"type\":\"error\",\"msg\":\"unknown type\"}");
        }
    }

    private void handleSend(Long me, JsonNode root) {
        long cid = root.path("cid").asLong();
        String cmid = root.path("cmid").asText(null);
        String msg = root.hasNonNull("msg") ? root.get("msg").asText() : null;
        String img = root.hasNonNull("img") ? root.get("img").asText() : null;

        Long peerId = chatService.ensureMember(me, cid);
        var saved = chatService.saveMessage(me, cid, msg, img);

        // ACK 给发送端
        sendToUser(me, "{\"type\":\"ack\",\"cid\":"+cid+",\"cmid\":\""+safe(cmid)+"\",\"mid\":"+saved.mid()+",\"time\":"+saved.time()+"}");

        // 推送给对端
        StringBuilder sb = new StringBuilder("{\"type\":\"message\",\"cid\":").append(cid)
                .append(",\"mid\":").append(saved.mid())
                .append(",\"from\":").append(me)
                .append(",\"time\":").append(saved.time());
        if (msg != null) sb.append(",\"msg\":\"").append(safe(msg)).append("\"");
        if (img != null) sb.append(",\"img\":\"").append(safe(img)).append("\"");
        sb.append("}");
        sendToUser(peerId, sb.toString());

        // 对端会话列表即时更新
        int unread = chatService.calcUnread(peerId, cid);
        sendToUser(peerId, "{\"type\":\"conv_update\",\"cid\":"+cid+",\"lastMessage\":\""+safe(saved.preview())+"\",\"lastTime\":"+saved.time()+",\"unread\":"+unread+"}");
    }

    private void handleRead(Long me, JsonNode root) {
        long cid = root.path("cid").asLong();
        long lastReadMid = root.path("lastReadMid").asLong();
        Long peerId = chatService.markRead(me, cid, lastReadMid);

        sendToUser(peerId, "{\"type\":\"read_notice\",\"cid\":"+cid+",\"lastReadMid\":"+lastReadMid+",\"from\":"+me+"}");
        // 自己未读清零
        sendToUser(me, "{\"type\":\"conv_update\",\"cid\":"+cid+",\"unread\":0}");
    }

    private void handleTyping(Long me, JsonNode root) {
        long cid = root.path("cid").asLong();
        Long peerId = chatService.getPeerId(me, cid);
        sendToUser(peerId, "{\"type\":\"typing\",\"cid\":"+cid+",\"from\":"+me+"}");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long uid = (Long) session.getAttributes().get("uid");
        Set<WebSocketSession> set = userSessions.get(uid);
        if (set != null) {
            set.remove(session);
            if (set.isEmpty()) userSessions.remove(uid);
        }
    }

    private void sendToUser(Long uid, String payload) {
        var set = userSessions.get(uid);
        if (set == null) return;
        for (var s : set) {
            if (s.isOpen()) try { s.sendMessage(new TextMessage(payload)); } catch (Exception ignore) {}
        }
    }

    private static String safe(String s) {
        if (s == null) return "";
        return s.replace("\\","\\\\").replace("\"","\\\"");
    }
}
