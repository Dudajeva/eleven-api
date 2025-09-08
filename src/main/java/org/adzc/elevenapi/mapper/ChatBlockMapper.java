package org.adzc.elevenapi.mapper;

import java.util.List;
import org.adzc.elevenapi.domain.ChatBlock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatBlockMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ChatBlock row);

    ChatBlock selectByPrimaryKey(Long id);

    List<ChatBlock> selectAll();

    int updateByPrimaryKey(ChatBlock row);
}