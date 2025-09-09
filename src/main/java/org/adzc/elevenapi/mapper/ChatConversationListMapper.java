package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.ChatMessageRead;
import org.adzc.elevenapi.message.dto.ConversationItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatConversationListMapper {
    List<ConversationItemDTO> list(@Param("me") Long me, @Param("limit") int limit, @Param("offset") int offset);
}