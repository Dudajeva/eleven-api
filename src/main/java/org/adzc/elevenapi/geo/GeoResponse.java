// src/main/java/org/adzc/elevenapi/geo/GeoResponse.java
package org.adzc.elevenapi.geo;

import java.util.List;

public class GeoResponse {
    public static class Region {
        private String name;
        private List<String> cities;
        public Region() {}
        public Region(String name, List<String> cities) { this.name = name; this.cities = cities; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public List<String> getCities() { return cities; }
        public void setCities(List<String> cities) { this.cities = cities; }
    }

    private String version;          // e.g. 20250829-120301
    private String updatedAt;        // ISO-8601
    private List<Region> regions;

    // ↓ 新增：缓存控制（任选其一即可，前端优先使用 expiresAt）
    private Long cacheTtlSeconds;    // 建议缓存秒数（如 604800 = 7 天）
    private Long expiresAt;          // 绝对过期时间（UTC 毫秒）

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public List<Region> getRegions() { return regions; }
    public void setRegions(List<Region> regions) { this.regions = regions; }

    public Long getCacheTtlSeconds() { return cacheTtlSeconds; }
    public void setCacheTtlSeconds(Long cacheTtlSeconds) { this.cacheTtlSeconds = cacheTtlSeconds; }
    public Long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Long expiresAt) { this.expiresAt = expiresAt; }
}
