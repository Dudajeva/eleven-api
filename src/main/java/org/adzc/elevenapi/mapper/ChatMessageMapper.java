package org.adzc.elevenapi.mapper;

import java.util.List;
import org.adzc.elevenapi.domain.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ChatMessage row);

    ChatMessage selectByPrimaryKey(Long id);

    List<ChatMessage> selectAll();

    int updateByPrimaryKey(ChatMessage row);

    int countUnread(@Param("cid") Long cid, @Param("uid") Long uid, @Param("cursor") Long cursor);
    List<ChatMessage> pageDesc(@Param("cid") Long cid, @Param("cursor") Long cursor, @Param("size") int size);
}