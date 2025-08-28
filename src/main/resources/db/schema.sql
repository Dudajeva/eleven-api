CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     identity VARCHAR(100) NOT NULL UNIQUE,   -- 邮箱或手机号（登录用）
                                     email VARCHAR(100),
                                     phone VARCHAR(30),
                                     password_hash VARCHAR(100) NOT NULL,     -- BCrypt
                                     nickname VARCHAR(50) NOT NULL,
                                     age INT,
                                     city VARCHAR(50),
                                     tier VARCHAR(20) DEFAULT 'normal',       -- normal/diamond/supreme
                                     photo_url VARCHAR(255),
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
