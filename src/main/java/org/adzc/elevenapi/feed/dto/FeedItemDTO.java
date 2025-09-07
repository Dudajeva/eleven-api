package org.adzc.elevenapi.feed.dto;
import lombok.Data;

@Data
public class FeedItemDTO {
    private Long id;
    private String nickname;
    private String avatarUrl;
    private Integer age;
    private String province;
    private String city;
    private String tier;      // 会员等级，若有就返回（可空）
    private String text;
    private String photoUrl;
    private Integer likeCount;
    private Boolean liked;
}
