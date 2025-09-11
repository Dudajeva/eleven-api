package org.adzc.elevenapi.message.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConversationItemDTO {
    private Long conversationId;
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private String province;
    private String city;
    private String tier;
    private String lastMessage;
    private LocalDateTime lastTime;
    private Integer unread;
}
