package org.adzc.elevenapi.mapper;

import java.util.List;
import org.adzc.elevenapi.domain.FeedLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FeedLikeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FeedLike row);

    FeedLike selectByPrimaryKey(Long id);

    List<FeedLike> selectAll();

    int updateByPrimaryKey(FeedLike row);

    int deleteLike(@Param("userId") Long userId,
                   @Param("postId") Long postId);
}