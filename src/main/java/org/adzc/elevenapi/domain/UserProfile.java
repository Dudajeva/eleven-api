package org.adzc.elevenapi.domain;

import java.time.LocalDate;

public class UserProfile {
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private String galleryJson;
    private Integer gender;       // 1男 2女
    private LocalDate birthday;
    private Integer heightCm;
    private Integer weightKg;
    private String city;
    private String profession;
    private String zodiac;
    private String hobbies;       // JSON 字符串
    private String drinking;      // 经常喝/偶尔喝/不喝酒
    private String wechat;

    public UserProfile() {}

    // getter / setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getGalleryJson() { return galleryJson; }
    public void setGalleryJson(String galleryJson) { this.galleryJson = galleryJson; }

    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    public Integer getHeightCm() { return heightCm; }
    public void setHeightCm(Integer heightCm) { this.heightCm = heightCm; }

    public Integer getWeightKg() { return weightKg; }
    public void setWeightKg(Integer weightKg) { this.weightKg = weightKg; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }

    public String getZodiac() { return zodiac; }
    public void setZodiac(String zodiac) { this.zodiac = zodiac; }

    public String getHobbies() { return hobbies; }
    public void setHobbies(String hobbies) { this.hobbies = hobbies; }

    public String getDrinking() { return drinking; }
    public void setDrinking(String drinking) { this.drinking = drinking; }

    public String getWechat() { return wechat; }
    public void setWechat(String wechat) { this.wechat = wechat; }
}
