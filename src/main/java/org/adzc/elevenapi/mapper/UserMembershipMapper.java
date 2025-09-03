package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.UserMembership;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMembershipMapper {
    UserMembership findByUserId(@Param("userId") Long userId);
    int insert(UserMembership m);
    int update(UserMembership m);

    // MySQL 专用 upsert（XML 实现）
    int upsertByUserId(UserMembership m);
}
