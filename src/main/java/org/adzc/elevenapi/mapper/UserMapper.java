package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User findByIdentity(@Param("identity") String identity);

    int insertUser(User user);

    long countUsers();
}
