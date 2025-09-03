package org.adzc.elevenapi.membership;

import org.adzc.elevenapi.domain.UserMembership;
import org.adzc.elevenapi.mapper.UserMembershipMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MembershipService {
    private final UserMembershipMapper mapper;

    public MembershipService(UserMembershipMapper mapper) {
        this.mapper = mapper;
    }

    public UserMembership getSnapshot(Long userId) {
        UserMembership m = mapper.findByUserId(userId);
        if (m == null) {
            m = new UserMembership();
            m.setUserId(userId);
            m.setTier("normal");
            m.setInviteLeft(0);
            m.setDmLeft(-1);
            m.setStatus("active");
            // 不立即插库，按需使用者可 seed；展示层默认即可
        }
        return m;
    }

    /** 便于本地/测试：直接写快照；生产可由回调/后台写入 */
    public void upsertSnapshot(UserMembership m) {
        if (m.getUserId() == null) throw new IllegalArgumentException("userId required");
        if (m.getTier() == null) m.setTier("normal");
        if (m.getInviteLeft() == null) m.setInviteLeft(0);
        if (m.getDmLeft() == null) m.setDmLeft(-1);
        if (m.getStatus() == null) m.setStatus("active");
        mapper.upsertByUserId(m);
    }
}
