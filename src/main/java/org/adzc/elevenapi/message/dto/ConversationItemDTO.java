package org.adzc.elevenapi.message.dto;

import java.time.LocalDateTime;

public record ConversationItemDTO(
        Long conversationId,
        Long userId,
        String nickname,
        String avatarUrl,
        String province,
        String city,
        String tier,
        String lastMessage,
        LocalDateTime lastTime,
        Integer unread
) {}
