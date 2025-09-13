package org.adzc.elevenapi.invitation;

import lombok.RequiredArgsConstructor;
import org.adzc.elevenapi.common.PageResult;
import org.adzc.elevenapi.domain.*;
import org.adzc.elevenapi.invitation.dto.InvitationCardDTO;
import org.adzc.elevenapi.invitation.dto.MyInvitationDTO;
import org.adzc.elevenapi.invitation.dto.SignUpResultDTO;
import org.adzc.elevenapi.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationMapper invitationMapper;
    private final InvitationSignUpMapper invitationSignUpMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserMembershipMapper userMembershipMapper;
    private final AppConfigMapper configMapper;

    public PageResult<InvitationCardDTO> getInvitationCards(int page, int size, Long currentUid) {
        int p = Math.max(page, 1);
        int s = Math.max(size, 1);
        int offset = (p - 1) * s;

        long total = invitationMapper.countAll();
        List<InvitationCardDTO> list = invitationMapper.selectInvitationCards(offset, s, currentUid);

        return new PageResult<>(list, p, s, total);
    }

    public List<MyInvitationDTO> myInvitations(Long currentUid) {
        // 1) 我发布的邀约
        List<Invitation> mine = invitationMapper.selectByUserId(currentUid);
        if (mine == null || mine.isEmpty()) return List.of();

        // 2) 补充报名用户与人数
        List<MyInvitationDTO> result = new ArrayList<>(mine.size());
        for (Invitation inv : mine) {
            int cnt = invitationSignUpMapper.countSignUps(inv.getId());
            List<UserProfile> profiles = invitationSignUpMapper.selectSignUpProfiles(inv.getId());
            result.add(new MyInvitationDTO(inv, profiles, cnt));
        }
        return result;
    }

    public Invitation invitationDetail(Long id) {
        return invitationMapper.selectByPrimaryKey(id);
    }

    @Transactional
    public Invitation createInvitation(Invitation invitation, Long publisherId) {
        if (invitation.getUserId() == null) {
            invitation.setUserId(publisherId);
        }
        invitationMapper.insertSelective(invitation); // 使用你生成器的 insertSelective
        return invitationMapper.selectByPrimaryKey(invitation.getId());
    }

    @Transactional
    public SignUpResultDTO signUp(Long invitationId, Long userId) {
        // 幂等
        boolean already = invitationSignUpMapper.exists(invitationId, userId) > 0;
        if (!already) {
            // 1) 读用户性别与会员等级
            UserProfile profile = userProfileMapper.selectByPrimaryKey(userId);
            UserMembership membership = userMembershipMapper.findByUserId(userId);   // normal/diamond/supreme

            // 2) 今日已用
            int used = invitationSignUpMapper.countTodayByUser(userId);

            // 3) 限额
            int limit = resolveDailyLimit(profile.getGender(), membership.getTier()); // 见下方方法

            if (used >= limit) {
                int count = invitationSignUpMapper.countByInvitation(invitationId);
                // 不抛 400，直接回传“已达上限”，前端走弹窗
                return new SignUpResultDTO(false, count);
            }

            // 4) 写报名
            InvitationSignUp signUp = new InvitationSignUp();
            signUp.setInvitationId(invitationId);
            signUp.setUserId(userId);
            invitationSignUpMapper.insertSelective(signUp);
        }
        int count = invitationSignUpMapper.countByInvitation(invitationId);
        return new SignUpResultDTO(true, count);
    }

    private int resolveDailyLimit(int gender, String tier) {
        // gender: "0" 女 / "1" 男（按你的 user_profile 定义）
        AppConfig dailyLimitConfig = null;
        if (0 == gender) {
            dailyLimitConfig = configMapper.selectByKey("signUp.limit.female");
        } else {
            // 男生分层读取
            String key = switch (String.valueOf(tier)) {
                case "diamond" -> "signUp.limit.male.diamond";
                case "supreme" -> "signUp.limit.male.supreme";
                default -> "signUp.limit.male.normal";
            };
            dailyLimitConfig = configMapper.selectByKey(key);
        }
        if (dailyLimitConfig == null) dailyLimitConfig = configMapper.selectByKey("signUp.limit.default");
        return Math.max(0, Integer.parseInt(dailyLimitConfig.getCfgValue()));
    }
}
