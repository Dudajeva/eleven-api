package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.InvitationSignUp;
import org.adzc.elevenapi.domain.UserProfile;
import org.adzc.elevenapi.home.dto.UserCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InvitationSignUpMapper {
    int deleteByPrimaryKey(Long id);

    int insert(InvitationSignUp row);

    int insertSelective(InvitationSignUp row);

    InvitationSignUp selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvitationSignUp row);

    int updateByPrimaryKey(InvitationSignUp row);

    int exists(@Param("invitationId") Long invitationId, @Param("userId") Long userId);
    int countByInvitation(@Param("invitationId") Long invitationId);
    int countTodayByUser(@Param("userId") Long userId);

    List<InvitationSignUp> selectByInvitation(@Param("invitationId") Long invitationId);

    List<InvitationSignUp> selectByInvitationIds(@Param("ids") List<Long> invitationIds);

    // 某个邀约的报名用户资料
    List<UserProfile> selectSignUpProfiles(@Param("invitationId") Long invitationId);

    // 某个邀约的报名数
    int countSignUps(@Param("invitationId") Long invitationId);

}