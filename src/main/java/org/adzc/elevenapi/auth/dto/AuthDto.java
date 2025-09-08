package org.adzc.elevenapi.auth.dto;

import lombok.Data;

@Data
public class AuthDto {
    private String token;
    private Long userId;
    private String nickname;
    private String identity;
    private Boolean firstLogin;
    private String avatarUrl;
    private String province;
    private String city;
    private String birthday;
    private String tier;
    private Integer age;

}
