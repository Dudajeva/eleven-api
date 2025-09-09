package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.ChatMessageRead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatMessageReadMapper {
    int deleteByPrimaryKey(@Param("messageId") Long messageId, @Param("userId") Long userId);

    int insert(ChatMessageRead row);

    int insertSelective(ChatMessageRead row);

    ChatMessageRead selectByPrimaryKey(@Param("messageId") Long messageId, @Param("userId") Long userId);

    int updateByPrimaryKeySelective(ChatMessageRead row);

    int updateByPrimaryKey(ChatMessageRead row);
}