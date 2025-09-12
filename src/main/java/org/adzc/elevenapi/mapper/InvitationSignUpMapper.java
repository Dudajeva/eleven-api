package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.InvitationSignUp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InvitationSignUpMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InvitationSignUp row);

    int insertSelective(InvitationSignUp row);

    InvitationSignUp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InvitationSignUp row);

    int updateByPrimaryKey(InvitationSignUp row);

    int exists(@Param("invitationId") Long invitationId, @Param("userId") Long userId);
    int countByInvitation(@Param("invitationId") Long invitationId);
    int countTodayByUser(@Param("userId") Long userId);

}