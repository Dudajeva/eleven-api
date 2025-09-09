package org.adzc.elevenapi.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMember {
    private Long id;

    private Long conversationId;

    private Long userId;

    private Long lastReadMessageId;

    private Integer isMuted;

    private Integer isPinned;

    private Date deletedAt;

    private Date joinedAt;

}