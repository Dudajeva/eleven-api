package org.adzc.elevenapi.domain;

import java.util.Date;

public class ChatConversation {
    private Long id;

    private Integer type;

    private Long u1Id;

    private Long u2Id;

    private Long lastMessageId;

    private String lastPreview;

    private Date lastTime;

    private Date createdAt;

    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getU1Id() {
        return u1Id;
    }

    public void setU1Id(Long u1Id) {
        this.u1Id = u1Id;
    }

    public Long getU2Id() {
        return u2Id;
    }

    public void setU2Id(Long u2Id) {
        this.u2Id = u2Id;
    }

    public Long getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public String getLastPreview() {
        return lastPreview;
    }

    public void setLastPreview(String lastPreview) {
        this.lastPreview = lastPreview;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}