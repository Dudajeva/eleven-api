package org.adzc.elevenapi.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
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

    private String intro;

    private String wechat;

    private Date updatedAt;

    private Boolean hidePhotos;

    private Boolean firstLogin;

    private String galleryJson;

    private String hobbies;

}