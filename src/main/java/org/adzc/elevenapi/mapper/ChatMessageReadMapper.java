package org.adzc.elevenapi.mapper;

import java.util.List;
import org.adzc.elevenapi.domain.ChatMessageRead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatMessageReadMapper {
    int deleteByPrimaryKey(@Param("messageId") Long messageId, @Param("userId") Long userId);

    int insert(ChatMessageRead row);

    ChatMessageRead selectByPrimaryKey(@Param("messageId") Long messageId, @Param("userId") Long userId);

    List<ChatMessageRead> selectAll();

    int updateByPrimaryKey(ChatMessageRead row);
}