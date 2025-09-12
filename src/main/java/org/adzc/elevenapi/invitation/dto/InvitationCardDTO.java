package org.adzc.elevenapi.invitation.dto;

import lombok.Data;
import java.util.Date;

@Data
public class InvitationCardDTO {
    private Long id;              // 邀约ID
    private Long publisherId;     // 发布者（= invitation.userId）

    // 邀约信息
    private String title;
    private String content;
    private String location;
    private String imageUrl;
    private Date createdAt;

    // 用户信息（来自 user_profile）
    private String nickname;
    private String avatarUrl;
    private Integer age;
    private String province;
    private String city;

    // 会员信息（来自 user_membership）
    private String tier;          // normal / diamond / supreme

    // 报名信息（增强）
    private Boolean signedUp;     // 当前登录用户是否已报名
    private Integer signUpCount;  // 总报名人数
}

