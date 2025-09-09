package org.adzc.elevenapi.message.dto;

import java.time.LocalDateTime;
import java.util.Date;

public record MessageDTO(
        Long id,
        Long conversationId,
        Long senderId,
        Integer type,          // 1=text 2=image
        String content,
        String imageUrl,
        Date createdAt
) {}
