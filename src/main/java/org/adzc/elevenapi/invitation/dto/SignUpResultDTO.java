package org.adzc.elevenapi.invitation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResultDTO {
    private boolean signedUp;
    private int signUpCount;
}
