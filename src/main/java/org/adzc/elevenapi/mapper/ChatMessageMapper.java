package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ChatMessage row);

    int insertSelective(ChatMessage row);

    ChatMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ChatMessage row);

    int updateByPrimaryKey(ChatMessage row);

    int countUnread(@Param("cid") Long cid, @Param("uid") Long uid, @Param("cursor") Long cursor);
    List<ChatMessage> pageDesc(@Param("cid") Long cid, @Param("cursor") Long cursor, @Param("size") int size);
}