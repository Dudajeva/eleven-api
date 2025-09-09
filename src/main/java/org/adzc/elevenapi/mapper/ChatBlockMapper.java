package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.ChatBlock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatBlockMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ChatBlock row);

    int insertSelective(ChatBlock row);

    ChatBlock selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ChatBlock row);

    int updateByPrimaryKey(ChatBlock row);
}