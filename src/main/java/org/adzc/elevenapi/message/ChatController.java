package org.adzc.elevenapi.message;

import org.adzc.elevenapi.auth.CurrentUid;
import org.adzc.elevenapi.message.dto.ConversationItemDTO;
import org.adzc.elevenapi.message.dto.MessageDTO;
import org.adzc.elevenapi.message.dto.OpenConversationResponse;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 会话列表
     */
    @GetMapping("/conversations")
    public List<ConversationItemDTO> conversations(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
            , @CurrentUid Long uid) {
        return chatService.listConversations(uid, page, size);
    }

    /**
     * 打开/创建会话
     */
    @PostMapping("/open")
    public OpenConversationResponse open(@RequestParam Long targetId, @CurrentUid Long uid) {
        Long cid = chatService.openOrCreate(uid, targetId);
        return new OpenConversationResponse(cid, targetId);
    }

    /**
     * 历史消息
     */
    @GetMapping("/messages")
    public List<MessageDTO> messages(
            @RequestParam Long conversationId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size
            , @CurrentUid Long uid) {
        return chatService.listMessages(uid, conversationId, cursor, size);
    }

    /**
     * 已读（HTTP 兜底）
     */
    @PostMapping("/read")
    public void read(
            @RequestParam Long conversationId,
            @RequestParam Long lastReadMessageId, @CurrentUid Long uid) {
        chatService.markRead(uid, conversationId, lastReadMessageId);
    }
}
