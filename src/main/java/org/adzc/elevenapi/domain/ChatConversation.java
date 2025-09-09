package org.adzc.elevenapi.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ChatConversation {
    private Long id;

    private Integer chatType;

    private Long u1Id;

    private Long u2Id;

    private Long lastMessageId;

    private String lastPreview;

    private Date lastTime;

    private Date createdAt;

    private Date updatedAt;

}