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
