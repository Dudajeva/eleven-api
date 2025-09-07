-- 用户表（如已存在，仅确保 gender 字段和唯一索引存在）
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     email VARCHAR(255) UNIQUE,
                                     phone VARCHAR(50) UNIQUE,
                                     password_hash VARCHAR(100) NOT NULL,
                                     nickname VARCHAR(100) NOT NULL,
                                     gender VARCHAR(10) NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 确保唯一约束存在（已存在时会报重复，忽略即可）
-- ALTER TABLE users ADD CONSTRAINT uk_users_email UNIQUE (email);

-- ALTER TABLE users ADD CONSTRAINT uk_users_phone UNIQUE (phone);


-- 统一地区表（省/直辖市/自治区 + 城市）
CREATE TABLE IF NOT EXISTS regions (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       name VARCHAR(50) NOT NULL,
    parent_id BIGINT NULL,                 -- 省级的 parent_id 为 NULL；城市为其所属省的 id
    level VARCHAR(10) NOT NULL,            -- 'province' | 'city'
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_level_name_parent (level, name, parent_id),
    KEY idx_parent (parent_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- user_profile：与 users 一对一
CREATE TABLE IF NOT EXISTS user_profile (
                                            user_id       BIGINT      NOT NULL PRIMARY KEY,       -- 与 users.id 对应
                                            nickname      VARCHAR(64) NOT NULL DEFAULT '',
    avatar_url    VARCHAR(255) DEFAULT NULL,               -- 头像
    gallery_json  JSON         DEFAULT NULL,               -- 相册数组 ["url1","url2",...]
    gender        TINYINT      DEFAULT NULL,               -- 1男 2女（可选用）
    birthday      DATE         DEFAULT NULL,               -- 生日（年龄由前端显示时计算）
    height_cm     SMALLINT     DEFAULT NULL,
    weight_kg     SMALLINT     DEFAULT NULL,
    city          VARCHAR(64)  DEFAULT NULL,
    profession    VARCHAR(64)  DEFAULT NULL,
    zodiac        VARCHAR(16)  DEFAULT NULL,               -- 星座
    hobbies       JSON         DEFAULT NULL,               -- 兴趣数组 ["健身","篮球",...]
    drinking      VARCHAR(16)  DEFAULT NULL,               -- "经常喝"/"偶尔喝"/"不喝酒"
    wechat        VARCHAR(64)  DEFAULT NULL,               -- 选填，不展示
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 快照表：我的页面直接读它
CREATE TABLE IF NOT EXISTS user_membership (
                                               id           BIGINT PRIMARY KEY AUTO_INCREMENT,
                                               user_id      BIGINT NOT NULL UNIQUE,
                                               tier         VARCHAR(16) NOT NULL DEFAULT 'normal',   -- normal/diamond/supreme
    start_time   DATETIME NULL,
    expire_time  DATETIME NULL,
    invite_left  INT NOT NULL DEFAULT 0,
    dm_left      INT NOT NULL DEFAULT -1,                 -- -1 = 无限
    auto_renew   TINYINT(1) NOT NULL DEFAULT 0,
    status       VARCHAR(16) NOT NULL DEFAULT 'active',   -- active/expired/suspended
    created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_um_user FOREIGN KEY (user_id) REFERENCES users(id)
    );
-- CREATE INDEX IF NOT EXISTS idx_um_expire ON user_membership (expire_time);

-- 历史周期（先建骨架，后续接收银台/后台使用）
CREATE TABLE IF NOT EXISTS user_membership_period (
                                                      id          BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                      user_id     BIGINT NOT NULL,
                                                      plan_code   VARCHAR(32) NULL,
    tier        VARCHAR(16) NOT NULL,
    start_time  DATETIME NOT NULL,
    end_time    DATETIME NOT NULL,
    source      VARCHAR(32) DEFAULT 'admin',              -- admin/stripe/wechat/alipay/gift…
    status      VARCHAR(16) DEFAULT 'success',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ump_user FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_ump_user_time (user_id, start_time)
    );


CREATE TABLE IF NOT EXISTS app_config (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            cfg_key   VARCHAR(100) NOT NULL UNIQUE,
                            cfg_value VARCHAR(500) NOT NULL,
                            remark    VARCHAR(255) NULL,
                            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 动态主表
CREATE TABLE feed_post (
                           id           BIGINT PRIMARY KEY AUTO_INCREMENT,
                           user_id      BIGINT      NOT NULL,
                           text         VARCHAR(1000) DEFAULT NULL,
                           photo_url    VARCHAR(512)  DEFAULT NULL,
                           created_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 点赞表（去重约束：同一用户对同一条只能点一次）
CREATE TABLE feed_like (
                           id         BIGINT PRIMARY KEY AUTO_INCREMENT,
                           post_id    BIGINT NOT NULL,
                           user_id    BIGINT NOT NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           UNIQUE KEY uk_post_user (post_id, user_id),
                           KEY idx_post (post_id),
                           KEY idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
