package org.adzc.elevenapi.home.dto;

import lombok.Data;

/**
 * 首页卡片精简视图（不暴露敏感字段）
 * 字段名与前端当前使用的 key 对齐：name / age / city / tier / photoUrl
 */
@Data
public class UserCard {
    private Long id;
    private String name;
    private Integer age;
    private String province;
    private String city;
    private String tier;
    private String photoUrl;
}
