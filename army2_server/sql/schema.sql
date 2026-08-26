-- ==========================================================
-- MOBI ARMY 2 OFFLINE & HYBRID VPS SERVER - SQL SCHEMA (MySQL / MariaDB / phpMyAdmin)
-- Database: army2_db
-- ==========================================================

DROP TABLE IF EXISTS `player_saves`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `missions`;
DROP TABLE IF EXISTS `server_config`;

CREATE TABLE IF NOT EXISTS `users` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(64) UNIQUE NOT NULL,
    `password` VARCHAR(64) NOT NULL,
    `email` VARCHAR(128) DEFAULT NULL,
    `role` VARCHAR(16) DEFAULT 'player',
    `xu` INT DEFAULT 50000,
    `luong` INT DEFAULT 100,
    `level` INT DEFAULT 1,
    `exp` BIGINT DEFAULT 0,
    `cup` INT DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `last_login` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_banned` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `player_saves` (
    `user_id` INT PRIMARY KEY,
    `username` VARCHAR(64) NOT NULL,
    `save_blob` LONGBLOB,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_player_save_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `missions` (
    `id` INT NOT NULL,
    `level` INT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `require_count` INT NOT NULL,
    `reward_exp` INT NOT NULL,
    `reward_xu` INT NOT NULL,
    `reward_luong` INT NOT NULL,
    `reward_cup` INT NOT NULL,
    PRIMARY KEY (`id`, `level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `server_config` (
    `config_key` VARCHAR(64) PRIMARY KEY,
    `config_value` TEXT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================================================
-- DEFAULT SERVER CONFIGURATION
-- ==========================================================
REPLACE INTO `server_config` (`config_key`, `config_value`) VALUES 
('server_name', 'Mobi Army 2 Hybrid Server'),
('starter_xu', '50000'),
('starter_luong', '100'),
('event_exp_rate', '1.0'),
('admin_password', 'admin123');

-- ==========================================================
-- DEFAULT MISSIONS (ALL 12 GUNS & SPECIAL MISSIONS)
-- ==========================================================
REPLACE INTO `missions` (`id`, `level`, `name`, `require_count`, `reward_exp`, `reward_xu`, `reward_luong`, `reward_cup`) VALUES
-- General
(0, 1, 'Lv 1: Thắng 100 ván solo', 100, 1000, 5000, 10, 100),
(0, 2, 'Lv 2: Thắng 1000 ván solo', 1000, 10000, 50000, 50, 1000),
(0, 3, 'Lv 3: Thắng 10000 ván solo', 10000, 100000, 500000, 200, 10000),
(1, 1, 'Lv 1: Bắn 100.000 điểm HP', 100000, 5000, 10000, 20, 500),
(1, 2, 'Lv 2: Bắn 1.000.000 điểm HP', 1000000, 50000, 100000, 100, 2000),
(1, 3, 'Lv 3: Bắn 1.000.000.000 điểm HP', 1000000000, 500000, 1000000, 500, 8000),
(2, 1, 'Lv 1: Thắng 50 ván đấu UFO', 50, 2000, 10000, 20, 500),
(2, 2, 'Lv 2: Thắng 100 ván đấu UFO', 100, 20000, 50000, 50, 2000),
(2, 3, 'Lv 3: Thắng 200 ván đấu UFO', 200, 200000, 500000, 200, 8000),
(3, 1, 'Lv 1: Thắng 50 ván đấu Khí Cầu', 50, 2000, 10000, 20, 500),
(3, 2, 'Lv 2: Thắng 100 ván đấu Khí Cầu', 100, 20000, 50000, 50, 2000),
(3, 3, 'Lv 3: Thắng 200 ván đấu Khí Cầu', 200, 200000, 500000, 200, 8000),
(4, 1, 'Lv 1: Thắng 50 ván đấu Ma', 50, 2000, 10000, 20, 500),
(4, 2, 'Lv 2: Thắng 100 ván đấu Ma', 100, 20000, 50000, 50, 2000),
(4, 3, 'Lv 3: Thắng 200 ván đấu Ma', 200, 200000, 500000, 200, 8000),
(5, 1, 'Lv 1: Ném 200 quả B52', 200, 2000, 10000, 10, 200),
(5, 2, 'Lv 2: Ném 2000 quả B52', 2000, 20000, 50000, 50, 1000),
(5, 3, 'Lv 3: Ném 20000 quả B52', 20000, 200000, 500000, 200, 5000),
(6, 1, 'Lv 1: Tiêu diệt 200 Tarzan', 200, 2000, 10000, 10, 200),
(6, 2, 'Lv 2: Tiêu diệt 2000 Tarzan', 2000, 20000, 50000, 50, 1000),
(6, 3, 'Lv 3: Tiêu diệt 20000 Tarzan', 20000, 200000, 500000, 200, 5000),
(7, 1, 'Lv 1: Tiêu diệt 200 Chicky', 200, 2000, 10000, 10, 200),
(7, 2, 'Lv 2: Tiêu diệt 2000 Chicky', 2000, 20000, 50000, 50, 1000),
(7, 3, 'Lv 3: Tiêu diệt 20000 Chicky', 20000, 200000, 500000, 200, 5000),
(8, 1, 'Lv 1: Tiêu diệt 200 Magenta', 200, 2000, 10000, 10, 200),
(8, 2, 'Lv 2: Tiêu diệt 2000 Magenta', 2000, 20000, 50000, 50, 1000),
(8, 3, 'Lv 3: Tiêu diệt 20000 Magenta', 20000, 200000, 500000, 200, 5000),
(9, 1, 'Lv 1: Tạo 1 viên ngọc cấp 8', 1, 1000, 10000, 10, 100),
(9, 2, 'Lv 2: Tạo 5 viên ngọc cấp 8', 5, 10000, 50000, 50, 500),
(9, 3, 'Lv 3: Tạo 10 viên ngọc cấp 8', 10, 100000, 500000, 200, 1000),
(10, 1, 'Lv 1: Tạo 1 viên ngọc cấp 9', 1, 5000, 20000, 20, 500),
(10, 2, 'Lv 2: Tạo 5 viên ngọc cấp 9', 5, 50000, 200000, 100, 5000),
(10, 3, 'Lv 3: Tạo 10 viên ngọc cấp 9', 10, 500000, 1000000, 500, 50000),
(11, 1, 'Lv 1: Tạo 1 viên ngọc cấp 10', 1, 50000, 100000, 100, 5000),
(11, 2, 'Lv 2: Tạo 5 viên ngọc cấp 10', 5, 500000, 1000000, 500, 50000),
(11, 3, 'Lv 3: Tạo 10 viên ngọc cấp 10', 10, 5000000, 10000000, 2000, 500000),
(12, 1, 'Lv 1: Bắn 100 phát siêu cao, siêu xa', 100, 5000, 10000, 10, 100),
(12, 2, 'Lv 2: Bắn 1.000 phát siêu cao, siêu xa', 1000, 25000, 50000, 50, 1000),
(12, 3, 'Lv 3: Bắn 10.000 phát siêu cao, siêu xa', 10000, 125000, 500000, 200, 10000),
-- Gun 0: Gunner
(13, 1, 'Lv 1: Dùng Gunner thắng 5 ván', 5, 5000, 10000, 50, 200),
(13, 2, 'Lv 2: Dùng Gunner thắng 50 ván', 50, 50000, 50000, 200, 1000),
(13, 3, 'Lv 3: Dùng Gunner thắng 500 ván', 500, 500000, 500000, 1000, 5000),
-- Gun 1: Miss 6
(14, 1, 'Lv 1: Dùng Miss 6 thắng 5 ván', 5, 5000, 10000, 50, 200),
(14, 2, 'Lv 2: Dùng Miss 6 thắng 50 ván', 50, 50000, 50000, 200, 1000),
(14, 3, 'Lv 3: Dùng Miss 6 thắng 500 ván', 500, 500000, 500000, 1000, 5000),
-- Gun 2: Electician
(15, 1, 'Lv 1: Dùng Electician thắng 5 ván', 5, 5000, 10000, 50, 200),
(15, 2, 'Lv 2: Dùng Electician thắng 50 ván', 50, 50000, 50000, 200, 1000),
(15, 3, 'Lv 3: Dùng Electician thắng 500 ván', 500, 500000, 500000, 1000, 5000),
-- Gun 3: King Kong
(16, 1, 'Lv 1: Dùng King Kong thắng 5 ván', 5, 5000, 10000, 50, 200),
(16, 2, 'Lv 2: Dùng King Kong thắng 50 ván', 50, 50000, 50000, 200, 1000),
(16, 3, 'Lv 3: Dùng King Kong thắng 500 ván', 500, 500000, 500000, 1000, 5000),
-- Gun 4: Rocketer
(17, 1, 'Lv 1: Dùng Rocketer thắng 5 ván', 5, 5000, 10000, 50, 200),
(17, 2, 'Lv 2: Dùng Rocketer thắng 50 ván', 50, 50000, 50000, 200, 1000),
(17, 3, 'Lv 3: Dùng Rocketer thắng 500 ván', 500, 500000, 500000, 1000, 5000),
-- Gun 5: Granos
(18, 1, 'Lv 1: Dùng Granos thắng 5 ván', 5, 5000, 10000, 50, 200),
(18, 2, 'Lv 2: Dùng Granos thắng 50 ván', 50, 50000, 50000, 200, 1000),
(18, 3, 'Lv 3: Dùng Granos thắng 500 ván', 500, 500000, 500000, 1000, 5000),
-- Gun 6: Chicky
(19, 1, 'Lv 1: Dùng Chicky thắng 5 ván', 5, 5000, 10000, 50, 200),
(19, 2, 'Lv 2: Dùng Chicky thắng 50 ván', 50, 50000, 50000, 200, 1000),
(19, 3, 'Lv 3: Dùng Chicky thắng 500 ván', 500, 500000, 500000, 1000, 5000),
-- Gun 7: Tarzan
(20, 1, 'Lv 1: Dùng Tarzan thắng 5 ván', 5, 5000, 10000, 50, 200),
(20, 2, 'Lv 2: Dùng Tarzan thắng 50 ván', 50, 50000, 50000, 200, 1000),
(20, 3, 'Lv 3: Dùng Tarzan thắng 500 ván', 500, 500000, 500000, 1000, 5000),
-- Gun 8: Apache
(21, 1, 'Lv 1: Dùng Apache thắng 5 ván', 5, 5000, 10000, 50, 200),
(21, 2, 'Lv 2: Dùng Apache thắng 50 ván', 50, 50000, 50000, 200, 1000),
(21, 3, 'Lv 3: Dùng Apache thắng 500 ván', 500, 500000, 500000, 1000, 5000),
-- Gun 9: Magenta
(22, 1, 'Lv 1: Dùng Magenta thắng 5 ván', 5, 5000, 10000, 50, 200),
(22, 2, 'Lv 2: Dùng Magenta thắng 50 ván', 50, 50000, 50000, 200, 1000),
(22, 3, 'Lv 3: Dùng Magenta thắng 500 ván', 500, 500000, 500000, 1000, 5000),
-- Gun 10: Draby
(23, 1, 'Lv 1: Dùng Draby thắng 5 ván', 5, 5000, 10000, 50, 200),
(23, 2, 'Lv 2: Dùng Draby thắng 50 ván', 50, 50000, 50000, 200, 1000),
(23, 3, 'Lv 3: Dùng Draby thắng 500 ván', 500, 500000, 500000, 1000, 5000),
-- Gun 11: Cow Girl
(24, 1, 'Lv 1: Dùng Cow Girl thắng 5 ván', 5, 5000, 10000, 50, 200),
(24, 2, 'Lv 2: Dùng Cow Girl thắng 50 ván', 50, 50000, 50000, 200, 1000),
(24, 3, 'Lv 3: Dùng Cow Girl thắng 500 ván', 500, 500000, 500000, 1000, 5000),
-- Other
(25, 1, 'Lv 1: Đăng nhập 30 ngày', 30, 10000, 50000, 50, 500),
(25, 2, 'Lv 2: Đăng nhập 90 ngày', 90, 30000, 150000, 150, 1500),
(25, 3, 'Lv 3: Đăng nhập 270 ngày', 270, 90000, 450000, 450, 4500),
(26, 1, 'Lv 1: Thắng 500 ván đấu trên 5 người chơi', 500, 5000, 50000, 50, 500),
(26, 2, 'Lv 2: Thắng 1000 ván đấu trên 5 người chơi', 1000, 10000, 100000, 100, 1000),
(26, 3, 'Lv 3: Thắng 2000 ván đấu trên 5 người chơi', 2000, 20000, 200000, 200, 2000),
(27, 1, 'Liên kết tài khoản Cloud', 1, 1000, 1000, 100, 0);
