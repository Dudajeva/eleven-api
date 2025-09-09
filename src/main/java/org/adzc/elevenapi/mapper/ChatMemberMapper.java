package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.ChatMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatMemberMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ChatMember row);

    int insertSelective(ChatMember row);

    ChatMember selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ChatMember row);

    int updateByPrimaryKey(ChatMember row);

    ChatMember find(@Param("cid") Long conversationId, @Param("uid") Long userId);
    Boolean exists(@Param("cid") Long conversationId, @Param("uid") Long userId);
    int updateLastRead(@Param("cid") Long conversationId, @Param("uid") Long userId, @Param("lastRead") Long lastRead);
    Long getLastRead(@Param("cid") Long conversationId, @Param("uid") Long userId);

    int insertMember(@Param("cid") Long conversationId, @Param("uid") Long userId);
}