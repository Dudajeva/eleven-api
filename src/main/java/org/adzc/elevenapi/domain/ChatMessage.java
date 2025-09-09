package org.adzc.elevenapi.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage {
    private Long id;

    private Long conversationId;

    private Long senderId;

    private Integer messageType;

    private String content;

    private String imageUrl;

    private Date createdAt;

    private Date recallAt;

    private Date deletedAt;

}