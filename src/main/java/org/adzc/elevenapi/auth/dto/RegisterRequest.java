package org.adzc.elevenapi.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {
    @NotBlank
    private String identity;   // 邮箱/手机号
    @NotBlank
    private String password;
    @NotBlank
    private String gender;     // male/female
    private String nickname;

    // getter/setter
    public String getIdentity() { return identity; }
    public void setIdentity(String identity) { this.identity = identity; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}
