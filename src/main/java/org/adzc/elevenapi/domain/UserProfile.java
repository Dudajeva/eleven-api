package org.adzc.elevenapi.domain;

import java.time.LocalDate;
import java.util.Date;

public class UserProfile {
    private Long userId;

    private String nickname;

    private String avatarUrl;

    private String photoUrl;

    private Integer gender;

    private Integer age;

    private Date birthday;

    private Short heightCm;

    private Short weightKg;

    private String province;

    private String city;

    private String profession;

    private String zodiac;

    private String drinking;

    private String wechat;

    private Date updatedAt;

    private Boolean hidePhotos;

    private Boolean firstLogin;

    private String galleryJson;

    private String hobbies;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Short getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Short heightCm) {
        this.heightCm = heightCm;
    }

    public Short getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Short weightKg) {
        this.weightKg = weightKg;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getDrinking() {
        return drinking;
    }

    public void setDrinking(String drinking) {
        this.drinking = drinking;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getHidePhotos() {
        return hidePhotos;
    }

    public void setHidePhotos(Boolean hidePhotos) {
        this.hidePhotos = hidePhotos;
    }

    public Boolean getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(Boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getGalleryJson() {
        return galleryJson;
    }

    public void setGalleryJson(String galleryJson) {
        this.galleryJson = galleryJson;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }
}