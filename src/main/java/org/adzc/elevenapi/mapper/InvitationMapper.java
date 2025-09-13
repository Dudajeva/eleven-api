package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.Invitation;
import org.adzc.elevenapi.invitation.dto.InvitationCardDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InvitationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Invitation row);

    int insertSelective(Invitation row);

    Invitation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Invitation row);


    int updateByPrimaryKey(Invitation row);


    long countAll();

    List<InvitationCardDTO> selectInvitationCards(@Param("offset") int offset,
                                                  @Param("size") int size,
                                                  @Param("currentUid") Long currentUid);

    List<Invitation> selectByUserId(@Param("currentUid") Long currentUid);
}