CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     identity VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100),
    phone VARCHAR(30),
    password_hash VARCHAR(100) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    age INT,
    province VARCHAR(50),             -- 新增：省份
    city VARCHAR(50),
    tier VARCHAR(20) DEFAULT 'normal',
    photo_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
