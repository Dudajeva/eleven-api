package org.adzc.elevenapi.auth.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.adzc.elevenapi.domain.User;
import org.adzc.elevenapi.domain.UserMembership;
import org.adzc.elevenapi.domain.UserProfile;

@Data
@RequiredArgsConstructor
public  class LoginResult {
    private final String token;
    private final User user;
    private final UserProfile userProfile;
    private final UserMembership userMembership;


    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
