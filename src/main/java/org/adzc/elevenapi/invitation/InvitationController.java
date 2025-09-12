// src/main/java/org/adzc/elevenapi/invitation/InvitationController.java
package org.adzc.elevenapi.invitation;

import lombok.RequiredArgsConstructor;
import org.adzc.elevenapi.auth.CurrentUid;
import org.adzc.elevenapi.common.PageResult;
import org.adzc.elevenapi.domain.Invitation;
import org.adzc.elevenapi.invitation.dto.InvitationCardDTO;
import org.adzc.elevenapi.invitation.dto.SignUpResultDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    // 列表分页（无筛选）
    @GetMapping("/list")
    public PageResult<InvitationCardDTO> list(
            @RequestParam int page,
            @RequestParam int size,
            @CurrentUid Long currentUid
    ) {
        return invitationService.getInvitationCards(page, size, currentUid);
    }

    // 发布
    @PostMapping("/create")
    public Invitation create(@RequestBody Invitation invitation, @CurrentUid Long currentUid) {
        return invitationService.createInvitation(invitation, currentUid);
    }

    // 报名（回传是否已报名+最新人数）
    @PostMapping("/signUp")
    public SignUpResultDTO signUp(
            @RequestParam(value = "invitationId") Long invitationId,
            @CurrentUid Long userId
    ) {
        if (invitationId == null) {
            throw new IllegalArgumentException("invitationId is required");
        }
        return invitationService.signUp(invitationId, userId);
    }
}
