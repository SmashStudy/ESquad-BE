-- book_qna_board: table
CREATE TABLE IF NOT EXISTS `book_qna_board` (
                                  `likes` int DEFAULT NULL,
                                  `book_id` bigint DEFAULT NULL,
                                  `created_at` datetime(6) DEFAULT NULL,
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `modified_at` datetime(6) DEFAULT NULL,
                                  `team_space_id` bigint DEFAULT NULL,
                                  `writer_id` bigint NOT NULL,
                                  `title` varchar(30) NOT NULL,
                                  `content` text NOT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `FK9f6o7xeoj3eumj68vkq88otsi` (`book_id`),
                                  KEY `FK5umppj6glc8sh2yo10d2xmegw` (`team_space_id`),
                                  KEY `FK5pfs8hcorg1e51m906kley92o` (`writer_id`),
                                  CONSTRAINT `FK5pfs8hcorg1e51m906kley92o` FOREIGN KEY (`writer_id`) REFERENCES `users` (`id`),
                                  CONSTRAINT `FK5umppj6glc8sh2yo10d2xmegw` FOREIGN KEY (`team_space_id`) REFERENCES `team_space` (`id`),
                                  CONSTRAINT `FK9f6o7xeoj3eumj68vkq88otsi` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- book_qna_like: table
CREATE TABLE IF NOT EXISTS `book_qna_like` (
                                 `board_id` bigint DEFAULT NULL,
                                 `created_at` datetime(6) DEFAULT NULL,
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `modified_at` datetime(6) DEFAULT NULL,
                                 `user_id` bigint NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `FKlayrndj93qyppcywu4snxlhy0` (`board_id`),
                                 KEY `FKqtwu5qcu1172xulsl1bbtsoqm` (`user_id`),
                                 CONSTRAINT `FKlayrndj93qyppcywu4snxlhy0` FOREIGN KEY (`board_id`) REFERENCES `book_qna_board` (`id`),
                                 CONSTRAINT `FKqtwu5qcu1172xulsl1bbtsoqm` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- book_qna_reply: table
CREATE TABLE IF NOT EXISTS `book_qna_reply` (
                                  `deleted_flag` tinyint(1) DEFAULT '0',
                                  `depth` int DEFAULT NULL,
                                  `likes` int DEFAULT NULL,
                                  `order_no` int DEFAULT NULL,
                                  `reply_flag` tinyint(1) DEFAULT '0',
                                  `board_id` bigint DEFAULT NULL,
                                  `created_at` datetime(6) DEFAULT NULL,
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `modified_at` datetime(6) DEFAULT NULL,
                                  `parent_reply_id` bigint DEFAULT NULL,
                                  `writer_id` bigint NOT NULL,
                                  `content` text,
                                  PRIMARY KEY (`id`),
                                  KEY `FKdhwbepmmda10985vvh34opef5` (`board_id`),
                                  KEY `FK91877w2at083s6st7kv44huyf` (`parent_reply_id`),
                                  KEY `FKc8edrxuhdnm06exbdhdr1wqpv` (`writer_id`),
                                  CONSTRAINT `FK91877w2at083s6st7kv44huyf` FOREIGN KEY (`parent_reply_id`) REFERENCES `book_qna_reply` (`id`),
                                  CONSTRAINT `FKc8edrxuhdnm06exbdhdr1wqpv` FOREIGN KEY (`writer_id`) REFERENCES `users` (`id`),
                                  CONSTRAINT `FKdhwbepmmda10985vvh34opef5` FOREIGN KEY (`board_id`) REFERENCES `book_qna_board` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- book_qna_reply_like: table
CREATE TABLE IF NOT EXISTS `book_qna_reply_like` (
                                       `created_at` datetime(6) DEFAULT NULL,
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `modified_at` datetime(6) DEFAULT NULL,
                                       `reply_id` bigint DEFAULT NULL,
                                       `user_id` bigint NOT NULL,
                                       PRIMARY KEY (`id`),
                                       KEY `FKt9en7dcx4imdfsoy5agc03a6k` (`reply_id`),
                                       KEY `FK1cwa605fi9bn908r9wmni68kl` (`user_id`),
                                       CONSTRAINT `FK1cwa605fi9bn908r9wmni68kl` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                                       CONSTRAINT `FKt9en7dcx4imdfsoy5agc03a6k` FOREIGN KEY (`reply_id`) REFERENCES `book_qna_reply` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- books: table
CREATE TABLE IF NOT EXISTS `books` (
                         `created_at` datetime(6) DEFAULT NULL,
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `modified_at` datetime(6) DEFAULT NULL,
                         `pub_date` varchar(8) NOT NULL,
                         `isbn` varchar(15) NOT NULL,
                         `author` varchar(50) NOT NULL,
                         `publisher` varchar(50) NOT NULL,
                         `title` varchar(150) NOT NULL,
                         `img_path` varchar(200) NOT NULL,
                         `description` text,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- notification: table
CREATE TABLE IF NOT EXISTS `notification` (
                                `read_flag` tinyint(1) DEFAULT '0',
                                `created_at` datetime(6) DEFAULT NULL,
                                `destination_user_id` bigint DEFAULT NULL,
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `modified_at` datetime(6) DEFAULT NULL,
                                `noti_type` varchar(20) DEFAULT NULL,
                                `message` varchar(50) NOT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FKhos70aihctfmaa5g2aguhr4bw` (`destination_user_id`),
                                CONSTRAINT `FKhos70aihctfmaa5g2aguhr4bw` FOREIGN KEY (`destination_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- stored_files: table
CREATE TABLE IF NOT EXISTS `stored_files` (
                                `file_size` int DEFAULT NULL,
                                `created_at` datetime(6) DEFAULT NULL,
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `modified_at` datetime(6) DEFAULT NULL,
                                `target_id` bigint DEFAULT NULL,
                                `user_id` bigint NOT NULL,
                                `file_path` varchar(255) DEFAULT NULL,
                                `origin_file_name` varchar(255) DEFAULT NULL,
                                `stored_file_name` varchar(255) DEFAULT NULL,
                                `file_type` enum('CSV','GIF','HWP','JPEG','JPG','PDF','PNG','TXT','XLSX') DEFAULT NULL,
                                `target_type` enum('BOOK_PAGE','QNA') DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FKc0pxhjsng9h58ifnju7u0hol3` (`user_id`),
                                CONSTRAINT `FKc0pxhjsng9h58ifnju7u0hol3` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- streaming_participants: table
CREATE TABLE IF NOT EXISTS `streaming_participants` (
                                          `voice_participation_flag` tinyint(1) DEFAULT '0',
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `session_id` bigint NOT NULL,
                                          `user_id` bigint NOT NULL,
                                          PRIMARY KEY (`id`),
                                          KEY `FK5kbq0ufw3twdvgaf2wp6itub0` (`session_id`),
                                          KEY `FKne8nels32jcgw68lgksp8xw50` (`user_id`),
                                          CONSTRAINT `FK5kbq0ufw3twdvgaf2wp6itub0` FOREIGN KEY (`session_id`) REFERENCES `streaming_sessions` (`id`),
                                          CONSTRAINT `FKne8nels32jcgw68lgksp8xw50` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- streaming_sessions: table
CREATE TABLE IF NOT EXISTS `streaming_sessions` (
                                      `channel_id` bigint NOT NULL,
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `user_id` bigint NOT NULL,
                                      `stream_type` varchar(10) NOT NULL,
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `UK2wrcv8yj5hwd1jh5fp8b0o80b` (`channel_id`),
                                      KEY `FKof8e0eqv10fbjknwyspk45ot4` (`user_id`),
                                      CONSTRAINT `FKof8e0eqv10fbjknwyspk45ot4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                                      CONSTRAINT `FKoyapco19xfe3aiyj6r201q1o3` FOREIGN KEY (`channel_id`) REFERENCES `study_page` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- study_page: table
CREATE TABLE IF NOT EXISTS `study_page` (
                              `end_date` date DEFAULT NULL,
                              `start_date` date DEFAULT NULL,
                              `book_id` bigint NOT NULL,
                              `created_at` datetime(6) DEFAULT NULL,
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `modified_at` datetime(6) DEFAULT NULL,
                              `team_space_id` bigint NOT NULL,
                              `description` text,
                              PRIMARY KEY (`id`),
                              KEY `FKpowbwi4ffy5uolsqtrf4vk9oo` (`book_id`),
                              KEY `FKd604urkvcr2jl26nokp0ctalp` (`team_space_id`),
                              CONSTRAINT `FKd604urkvcr2jl26nokp0ctalp` FOREIGN KEY (`team_space_id`) REFERENCES `team_space` (`id`),
                              CONSTRAINT `FKpowbwi4ffy5uolsqtrf4vk9oo` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- study_page_users: table
CREATE TABLE IF NOT EXISTS `study_page_users` (
                                    `owner_flag` tinyint(1) DEFAULT '0',
                                    `created_at` datetime(6) DEFAULT NULL,
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `modified_at` datetime(6) DEFAULT NULL,
                                    `study_page_id` bigint DEFAULT NULL,
                                    `user_id` bigint DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `FK9codfgy531yrsdo5tfcoehwb3` (`user_id`),
                                    KEY `FKki41d0ijhhrl1j97dueokv4r5` (`study_page_id`),
                                    CONSTRAINT `FK9codfgy531yrsdo5tfcoehwb3` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                                    CONSTRAINT `FKki41d0ijhhrl1j97dueokv4r5` FOREIGN KEY (`study_page_id`) REFERENCES `study_page` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- study_remind: table
CREATE TABLE IF NOT EXISTS `study_remind` (
                                `day_at` tinyint NOT NULL,
                                `remind_flag` tinyint(1) DEFAULT '1',
                                `time_at` time(6) DEFAULT NULL,
                                `created_at` datetime(6) DEFAULT NULL,
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `modified_at` datetime(6) DEFAULT NULL,
                                `study_page_id` bigint DEFAULT NULL,
                                `description` varchar(100) DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FK88yb0dya1x294mtag3iiof5b4` (`study_page_id`),
                                CONSTRAINT `FK88yb0dya1x294mtag3iiof5b4` FOREIGN KEY (`study_page_id`) REFERENCES `study_page` (`id`),
                                CONSTRAINT `study_remind_chk_1` CHECK ((`day_at` between 0 and 6))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- team_space: table
CREATE TABLE IF NOT EXISTS `team_space` (
                              `created_at` datetime(6) DEFAULT NULL,
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `modified_at` datetime(6) DEFAULT NULL,
                              `team_name` varchar(100) NOT NULL,
                              `description` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `UKhl3ncky1ewsor5nvrgjv597pm` (`team_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- team_space_users: table
CREATE TABLE IF NOT EXISTS `team_space_users` (
                                    `created_at` datetime(6) DEFAULT NULL,
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `modified_at` datetime(6) DEFAULT NULL,
                                    `team_space_id` bigint DEFAULT NULL,
                                    `username` bigint DEFAULT NULL,
                                    `role` varchar(20) NOT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `FK4dpyy47asw7x1i1nvp4l8r6lx` (`username`),
                                    KEY `FK880phws69n6x3x1326tnhsdge` (`team_space_id`),
                                    CONSTRAINT `FK4dpyy47asw7x1i1nvp4l8r6lx` FOREIGN KEY (`username`) REFERENCES `users` (`id`),
                                    CONSTRAINT `FK880phws69n6x3x1326tnhsdge` FOREIGN KEY (`team_space_id`) REFERENCES `team_space` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- user_settings: table
CREATE TABLE IF NOT EXISTS `user_settings` (
                                 `voice_flag` tinyint(1) DEFAULT '0',
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `user_id` bigint NOT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `UK4bos7satl9xeqd18frfeqg6tt` (`user_id`),
                                 CONSTRAINT `FK8v82nj88rmai0nyck19f873dw` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- users: table
CREATE TABLE IF NOT EXISTS `users` (
                         `birth_day` date NOT NULL,
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `phone_no` varchar(12) NOT NULL,
                         `nickname` varchar(20) NOT NULL,
                         `username` varchar(20) NOT NULL,
                         `email` varchar(25) NOT NULL,
                         `password` varchar(60) NOT NULL,
                         `address` varchar(255) NOT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK2ty1xmrrgtn89xt7kyxx6ta7h` (`nickname`),
                         UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

