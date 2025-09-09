package org.adzc.elevenapi.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessageRead {
    private Long messageId;

    private Long userId;

    private Date readAt;
}