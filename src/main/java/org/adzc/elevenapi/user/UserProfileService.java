package org.adzc.elevenapi.user;

import org.adzc.elevenapi.domain.User;
import org.adzc.elevenapi.domain.UserProfile;
import org.adzc.elevenapi.mapper.UserMapper;
import org.adzc.elevenapi.mapper.UserProfileMapper;
import org.adzc.elevenapi.util.DateUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserProfileService {

    private final UserProfileMapper userProfileMapper;
    private final UserMapper userMapper;


    public UserProfileService(UserProfileMapper userProfileMapper, UserMapper userMapper) {
        this.userProfileMapper = userProfileMapper;
        this.userMapper = userMapper;
    }

    public UserProfile getOrInit(long userId) {
        UserProfile userProfile = userProfileMapper.selectByPrimaryKey(userId);
        return userProfile;
    }

    public void save(UserProfile userProfile) {
        if (userProfile.getUserId() == null) throw new IllegalArgumentException("userId is required");
        if (userProfile.getNickname() == null) userProfile.setNickname("");
        userProfile.setAge(DateUtil.ageOn(userProfile.getBirthday(), LocalDate.now()));
        userProfile.setFirstLogin(false);

        User user=userMapper.selectByPrimaryKey(userProfile.getUserId());
        if (user==null) throw new IllegalArgumentException("user is null");
        userProfileMapper.updateByPrimaryKey(userProfile);
    }

    public void updateAvatar(long userId, String url) {
        userProfileMapper.updateAvatar(userId, url);
    }

    public void updateHidePhotos(long userId, boolean hide) {
        userProfileMapper.updateHidePhotos(userId, hide);
    }
}
