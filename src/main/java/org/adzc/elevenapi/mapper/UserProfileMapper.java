package org.adzc.elevenapi.mapper;

import java.util.List;
import org.adzc.elevenapi.domain.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserProfileMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(UserProfile row);

    UserProfile selectByPrimaryKey(Long userId);

    List<UserProfile> selectAll();

    int updateByPrimaryKey(UserProfile row);

    int updateAvatar(@Param("userId") Long userId, @Param("avatarUrl") String avatarUrl);

    int updateHidePhotos(@Param("uid") Long uid, @Param("hide") boolean hide); // 新增

}