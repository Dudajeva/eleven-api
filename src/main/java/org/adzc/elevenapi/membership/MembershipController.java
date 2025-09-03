package org.adzc.elevenapi.membership;

import jakarta.servlet.http.HttpServletRequest;
import org.adzc.elevenapi.auth.CurrentUid;
import org.adzc.elevenapi.domain.UserMembership;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * /api/membership 仅提供“查询我的会员信息”
 * 另提供一个 upsert DEV 接口，方便你在没有后台时临时写入（可根据配置关掉）
 */
@RestController
@RequestMapping("/api/membership")
public class MembershipController {

    private final MembershipService service;

    public MembershipController(MembershipService service) {
        this.service = service;
    }


    /** 我的会员信息 */
    @GetMapping("/me")
    public Map<String, Object> me(@CurrentUid Long uid) {
        UserMembership m = service.getSnapshot(uid);

        Map<String, Object> dto = new HashMap<>();
        dto.put("tier", m.getTier());
        dto.put("expireTime", m.getExpireTime());   // 前端可用 dayjs 格式化为 YYYY-MM-DD
        dto.put("inviteLeft", m.getInviteLeft());
        dto.put("dmLeft", m.getDmLeft());
        return dto;
    }
}
