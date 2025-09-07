package org.adzc.elevenapi.mapper;

import java.util.List;
import org.adzc.elevenapi.domain.FeedPost;
import org.adzc.elevenapi.feed.dto.FeedItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FeedPostMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FeedPost row);

    FeedPost selectByPrimaryKey(Long id);

    List<FeedPost> selectAll();

    int updateByPrimaryKey(FeedPost row);

    List<FeedItemDTO> list(@Param("viewerId") Long viewerId,
                           @Param("offset") int offset,
                           @Param("size") int size,
                           @Param("province") String province,
                           @Param("city") String city);
}