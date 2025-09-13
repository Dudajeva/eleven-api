package org.adzc.elevenapi.invitation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.adzc.elevenapi.domain.Invitation;
import org.adzc.elevenapi.domain.UserProfile;

import java.util.List;

@Data
@AllArgsConstructor
public class MyInvitationDTO {

    private Invitation invitation;
    private List<UserProfile> signUpUserProfileList;
    private int signUpCount;
}
