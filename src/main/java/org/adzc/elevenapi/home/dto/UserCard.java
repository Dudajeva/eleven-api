package org.adzc.elevenapi.home.dto;

/**
 * 首页卡片精简视图（不暴露敏感字段）
 * 字段名与前端当前使用的 key 对齐：name / age / city / tier / photoUrl
 */
public class UserCard {
    private Long id;
    private String name;
    private Integer age;
    private String province;
    private String city;
    private String tier;
    private String photoUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
