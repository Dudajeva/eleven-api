package org.adzc.elevenapi.domain;

import lombok.Data;

import java.util.Date;

@Data
public class InvitationSignUp {
    private Long id;

    private Long invitationId;

    private Long userId;

    private Date signUpTime;
}