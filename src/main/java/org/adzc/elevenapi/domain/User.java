package org.adzc.elevenapi.domain;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户实体，对应表 users
 */
public class User {

    private Long id;

    @NotBlank
    private String identity;       // 邮箱或手机号（登录唯一）

    private String email;
    private String phone;

    @NotBlank
    private String passwordHash;   // BCrypt

    @NotBlank
    private String nickname;

    private Integer age;
    private String city;
    private String tier;           // normal/diamond/supreme
    private String photoUrl;

    // getter/setter 省略可用 Lombok，这里手写以免你未装 Lombok
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIdentity() { return identity; }
    public void setIdentity(String identity) { this.identity = identity; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
