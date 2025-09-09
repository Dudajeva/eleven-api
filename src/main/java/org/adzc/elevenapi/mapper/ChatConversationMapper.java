package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.ChatConversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatConversationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ChatConversation row);

    int insertSelective(ChatConversation row);

    ChatConversation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ChatConversation row);

    int updateByPrimaryKey(ChatConversation row);


    ChatConversation findById(@Param("id") Long id);
    int updateLast(@Param("cid") Long conversationId, @Param("mid") Long lastMessageId, @Param("preview") String preview);

    Long findIdByUsers(@Param("u1") Long u1, @Param("u2") Long u2);
    int insertConversation(@Param("u1") Long u1, @Param("u2") Long u2); // useGeneratedKeys in XML
}