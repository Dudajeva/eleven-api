package org.adzc.elevenapi.mapper;

import java.util.List;
import org.adzc.elevenapi.domain.User;
import org.adzc.elevenapi.feed.UserCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User row);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User row);


    User findByIdentity(@Param("identity") String identity);

    long countUsers();

    // ===== Feed 用：分页查卡片视图 =====
    List<UserCard> selectUserCards(@Param("limit") int limit,
                                   @Param("offset") int offset);

    long countAllForFeed();

    /* 带省市筛选 */
    List<UserCard> selectUserCardsFiltered(@Param("limit") int limit,
                                           @Param("offset") int offset,
                                           @Param("province") String province,
                                           @Param("city") String city);

    long countForFeedFiltered(@Param("province") String province,
                              @Param("city") String city);
}