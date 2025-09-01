package org.adzc.elevenapi.common;

public class OssPolicyResp {
    private String host;        // 上传地址（表单 POST 到这里）
    private String dir;         // 允许上传的 key 前缀
    private String accessId;    // 临时 AK
    private String policy;      // Base64 编码策略
    private String signature;   // 签名
    private String securityToken; // STS token
    private long expire;        // 过期秒级时间戳

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public String getDir() { return dir; }
    public void setDir(String dir) { this.dir = dir; }
    public String getAccessId() { return accessId; }
    public void setAccessId(String accessId) { this.accessId = accessId; }
    public String getPolicy() { return policy; }
    public void setPolicy(String policy) { this.policy = policy; }
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
    public String getSecurityToken() { return securityToken; }
    public void setSecurityToken(String securityToken) { this.securityToken = securityToken; }
    public long getExpire() { return expire; }
    public void setExpire(long expire) { this.expire = expire; }
}
