package org.adzc.elevenapi.message.dto;

import java.time.LocalDateTime;

public record MessageDTO(
        Long id,
        Long conversationId,
        Long senderId,
        Byte type,          // 1=text 2=image
        String content,
        String imageUrl,
        LocalDateTime createdAt
) {}
