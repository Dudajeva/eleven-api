package org.adzc.elevenapi.domain;

import java.util.Date;

public class UserMembership {
    private Long id;
    private Long userId;
    private String tier;          // normal/diamond/supreme
    private Date startTime;
    private Date expireTime;
    private Integer inviteLeft;
    private Integer dmLeft;       // -1 = 无限
    private Boolean autoRenew;
    private String status;        // active/expired/suspended
    private Date createdAt;
    private Date updatedAt;

    public UserMembership() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getExpireTime() { return expireTime; }
    public void setExpireTime(Date expireTime) { this.expireTime = expireTime; }

    public Integer getInviteLeft() { return inviteLeft; }
    public void setInviteLeft(Integer inviteLeft) { this.inviteLeft = inviteLeft; }

    public Integer getDmLeft() { return dmLeft; }
    public void setDmLeft(Integer dmLeft) { this.dmLeft = dmLeft; }

    public Boolean getAutoRenew() { return autoRenew; }
    public void setAutoRenew(Boolean autoRenew) { this.autoRenew = autoRenew; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
