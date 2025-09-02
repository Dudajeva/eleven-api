package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserProfileMapper {
    UserProfile findByUserId(@Param("userId") Long userId);
    int upsert(UserProfile profile);
    int updateAvatar(@Param("userId") Long userId, @Param("avatarUrl") String avatarUrl);

    int updateHidePhotos(@Param("uid") Long uid, @Param("hide") boolean hide); // 新增
}
