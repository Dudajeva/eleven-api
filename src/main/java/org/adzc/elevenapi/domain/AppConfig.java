package org.adzc.elevenapi.domain;

import java.sql.Timestamp;

public class AppConfig {
    private Long id;
    private String cfgKey;
    private String cfgValue;
    private String remark;
    private Timestamp updatedAt;

    // Getter & Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCfgKey() {
        return cfgKey;
    }
    public void setCfgKey(String cfgKey) {
        this.cfgKey = cfgKey;
    }
    public String getCfgValue() {
        return cfgValue;
    }
    public void setCfgValue(String cfgValue) {
        this.cfgValue = cfgValue;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}