package org.adzc.elevenapi.message.controller;

import org.adzc.elevenapi.message.dto.ConversationItemDTO;
import org.adzc.elevenapi.message.dto.MessageDTO;
import org.adzc.elevenapi.message.dto.OpenConversationResponse;
import org.adzc.elevenapi.message.service.ChatService;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    public ChatController(ChatService chatService) { this.chatService = chatService; }

    /** 会话列表 */
    @GetMapping("/conversations")
    public List<ConversationItemDTO> conversations(HttpServletRequest req,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "20") int size) {
        Long me = (Long) req.getAttribute("auth.uid"); // 复用 JwtAuthFilter 注入的属性  :contentReference[oaicite:4]{index=4}
        return chatService.listConversations(me, page, size);
    }

    /** 打开/创建会话 */
    @PostMapping("/open")
    public OpenConversationResponse open(HttpServletRequest req, @RequestParam Long targetId) {
        Long me = (Long) req.getAttribute("auth.uid");
        Long cid = chatService.openOrCreate(me, targetId);
        return new OpenConversationResponse(cid, targetId);
    }

    /** 历史消息 */
    @GetMapping("/messages")
    public List<MessageDTO> messages(HttpServletRequest req,
                                     @RequestParam Long conversationId,
                                     @RequestParam(required = false) Long cursor,
                                     @RequestParam(defaultValue = "20") int size) {
        Long me = (Long) req.getAttribute("auth.uid");
        return chatService.listMessages(me, conversationId, cursor, size);
    }

    /** 已读（HTTP 兜底） */
    @PostMapping("/read")
    public void read(HttpServletRequest req,
                     @RequestParam Long conversationId,
                     @RequestParam Long lastReadMessageId) {
        Long me = (Long) req.getAttribute("auth.uid");
        chatService.markRead(me, conversationId, lastReadMessageId);
    }
}
