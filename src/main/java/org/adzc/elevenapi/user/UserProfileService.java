package org.adzc.elevenapi.user;

import org.adzc.elevenapi.domain.UserProfile;
import org.adzc.elevenapi.mapper.UserProfileMapper;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileMapper mapper;

    public UserProfileService(UserProfileMapper mapper) {
        this.mapper = mapper;
    }

    public UserProfile getOrInit(long userId) {
        UserProfile p = mapper.findByUserId(userId);
        if (p == null) {
            p = new UserProfile();
            p.setUserId(userId);
            p.setNickname("");
            mapper.upsert(p);
            p = mapper.findByUserId(userId);
        }
        return p;
    }

    public void save(UserProfile p) {
        if (p.getUserId() == null) throw new IllegalArgumentException("userId is required");
        if (p.getNickname() == null) p.setNickname("");
        mapper.upsert(p);
    }

    public void updateAvatar(long userId, String url) {
        mapper.updateAvatar(userId, url);
    }
}
