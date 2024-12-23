/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : question_bank

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 17/12/2024 12:32:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_users
-- ----------------------------
DROP TABLE IF EXISTS `admin_users`;
CREATE TABLE `admin_users`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'ACTIVE',
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色:ADMIN-超级管理员,OPERATOR-运营',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin_users
-- ----------------------------
INSERT INTO `admin_users` VALUES (4, 'admin', '$2a$10$JvvMTvrWIV4qTc/plomZdu9h7DDhJmHll1YyF5V7zPnFtyYeMicQ.', '超级管理员', NULL, NULL, NULL, 'ACTIVE', 'ADMIN', '2024-12-17 04:30:32', '2024-12-17 04:26:53', '2024-12-17 04:30:32');

-- ----------------------------
-- Table structure for bank_categories
-- ----------------------------
DROP TABLE IF EXISTS `bank_categories`;
CREATE TABLE `bank_categories`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '题库分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bank_categories
-- ----------------------------
INSERT INTO `bank_categories` VALUES (1, '计算机考研', '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, NULL, NULL, NULL);
INSERT INTO `bank_categories` VALUES (2, '计算机等级考试', '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, NULL, NULL, NULL);
INSERT INTO `bank_categories` VALUES (3, '英语等级考试', '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, NULL, NULL, NULL);
INSERT INTO `bank_categories` VALUES (4, '软考认证', '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  `sort_order` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of categories
-- ----------------------------

-- ----------------------------
-- Table structure for knowledge_points
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_points`;
CREATE TABLE `knowledge_points`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bank_id` bigint NOT NULL COMMENT '所属题库ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '知识点名称',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级知识点ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_bank_id`(`bank_id` ASC) USING BTREE,
  CONSTRAINT `knowledge_points_ibfk_1` FOREIGN KEY (`bank_id`) REFERENCES `question_banks` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of knowledge_points
-- ----------------------------
INSERT INTO `knowledge_points` VALUES (1, 1, '数据结构', NULL, '2024-11-15 19:39:56', '2024-11-15 19:39:56');
INSERT INTO `knowledge_points` VALUES (2, 1, '计算机组成原理', NULL, '2024-11-15 19:39:56', '2024-11-15 19:39:56');
INSERT INTO `knowledge_points` VALUES (3, 1, '操作系统', NULL, '2024-11-15 19:39:56', '2024-11-15 19:39:56');
INSERT INTO `knowledge_points` VALUES (4, 1, '计算机网络', NULL, '2024-11-15 19:39:56', '2024-11-15 19:39:56');

-- ----------------------------
-- Table structure for practice_progress
-- ----------------------------
DROP TABLE IF EXISTS `practice_progress`;
CREATE TABLE `practice_progress`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `bank_id` bigint NOT NULL COMMENT '题库ID',
  `total_questions` int NULL DEFAULT 0 COMMENT '总题目数',
  `finished_questions` int NULL DEFAULT 0 COMMENT '已完成题目数',
  `correct_count` int NULL DEFAULT 0 COMMENT '正确题目数',
  `last_question_id` bigint NULL DEFAULT NULL COMMENT '最后做到的题目ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_bank`(`user_id` ASC, `bank_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_bank_id`(`bank_id` ASC) USING BTREE,
  CONSTRAINT `fk_progress_bank` FOREIGN KEY (`bank_id`) REFERENCES `question_banks` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_progress_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '练习进度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of practice_progress
-- ----------------------------
INSERT INTO `practice_progress` VALUES (1, 28, 1, 0, 24, 4, 9, '2024-11-22 13:35:07', '2024-11-22 13:45:10');
INSERT INTO `practice_progress` VALUES (2, 28, 2, 0, 7, 1, 2, '2024-11-22 13:35:59', '2024-11-23 03:23:57');
INSERT INTO `practice_progress` VALUES (3, 29, 1, 0, 59, 14, 16, '2024-11-22 13:47:01', '2024-11-23 01:59:37');
INSERT INTO `practice_progress` VALUES (4, 29, 2, 0, 12, 3, 2, '2024-11-22 13:51:35', '2024-11-23 02:02:31');
INSERT INTO `practice_progress` VALUES (5, 28, 3, 0, 1, 0, 3, '2024-11-23 03:24:08', '2024-11-23 03:24:08');

-- ----------------------------
-- Table structure for practice_records
-- ----------------------------
DROP TABLE IF EXISTS `practice_records`;
CREATE TABLE `practice_records`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `bank_id` bigint NULL DEFAULT NULL,
  `mode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `total_questions` int NOT NULL,
  `correct_count` int NOT NULL,
  `spend_time` int NOT NULL,
  `correct_rate` double NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_time`(`user_id` ASC, `create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of practice_records
-- ----------------------------

-- ----------------------------
-- Table structure for question_banks
-- ----------------------------
DROP TABLE IF EXISTS `question_banks`;
CREATE TABLE `question_banks`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '题库名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '题库描述',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类ID',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `question_count` int NULL DEFAULT 0 COMMENT '题目数量',
  `user_count` int NULL DEFAULT 0 COMMENT '做题人数',
  `status` int NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `practice_count` int NULL DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  CONSTRAINT `question_banks_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `bank_categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '题库表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of question_banks
-- ----------------------------
INSERT INTO `question_banks` VALUES (1, '408综合真题', '计算机考研408综合历年真题，包含数据结构、计算机组成原理、操作系统、计算机网络等核心考点', 1, NULL, 10, 1200, 1, '2024-11-14 22:41:13', '2024-11-15 19:39:56', NULL, b'0');
INSERT INTO `question_banks` VALUES (2, '数据结构专项', '计算机考研数据结构专项练习，包括线性表、树、图、排序等重点知识点', 1, NULL, 300, 800, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (3, '操作系统专项', '计算机考研操作系统专项练习，覆盖进程管理、内存管理、文件系统等核心知识点', 1, NULL, 250, 600, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (4, '计算机网络专项', '计算机考研计算机网络专项练习，包含TCP/IP协议、网络安全等重要内容', 1, NULL, 280, 750, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (5, '二级C语言', '计算机等级考试二级C语言题库，包含基础语法、指针、结构体等内容', 2, NULL, 400, 2000, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (6, '二级Python', '计算机等级考试二级Python题库，包含基础语法、函数、面向对象等内容', 2, NULL, 350, 1800, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (7, '三级数据库', '计算机等级考试三级数据库技术题库，包含SQL、数据库设计等内容', 2, NULL, 300, 1200, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (8, '四级网络工程', '计算机等级考试四级网络工程师题库，包含网络规划、网络管理等内容', 2, NULL, 280, 800, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (9, '大学英语四级', '大学英语四级考试题库，包含听力、阅读、写作和翻译等内容', 3, NULL, 600, 5000, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (10, '大学英语六级', '大学英语六级考试题库，包含听力、阅读、写作和翻译等内容', 3, NULL, 500, 3000, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (11, '专业英语四级', '英语专业四级考试题库，包含听力、阅读、写作和翻译等专业内容', 3, NULL, 400, 1000, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (12, '专业英语八级', '英语专业八级考试题库，包含听力、阅读、写作和翻译等高级内容', 3, NULL, 350, 800, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (13, '软件设计师', '中级软件设计师考试题库，包含计算机基础、软件工程等内容', 4, NULL, 450, 2500, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (14, '系统架构设计师', '高级系统架构设计师考试题库，包含架构设计、设计模式等内容', 4, NULL, 400, 1500, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (15, '网络工程师', '中级网络工程师考试题库，包含网络技术、网络规划与设计等内容', 4, NULL, 380, 1800, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');
INSERT INTO `question_banks` VALUES (16, '信息安全工程师', '中级信息安全工程师考试题库，包含安全技术、安全管理等内容', 4, NULL, 360, 1600, 1, '2024-11-14 22:41:13', '2024-11-14 22:41:13', NULL, b'0');

-- ----------------------------
-- Table structure for question_collections
-- ----------------------------
DROP TABLE IF EXISTS `question_collections`;
CREATE TABLE `question_collections`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `question_id` bigint NOT NULL COMMENT '题目ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_question`(`user_id` ASC, `question_id` ASC) USING BTREE COMMENT '同一用户不能重复收藏同一题目',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_question_id`(`question_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `question_collections_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `question_collections_ibfk_2` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of question_collections
-- ----------------------------

-- ----------------------------
-- Table structure for question_options
-- ----------------------------
DROP TABLE IF EXISTS `question_options`;
CREATE TABLE `question_options`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `question_id` bigint NOT NULL COMMENT '题目ID',
  `key` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '选项标识，如A、B、C、D',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '选项内容',
  `is_correct` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是正确选项',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_question_id`(`question_id` ASC) USING BTREE,
  CONSTRAINT `question_options_ibfk_1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of question_options
-- ----------------------------
INSERT INTO `question_options` VALUES (1, 1, 'A', '14', 0);
INSERT INTO `question_options` VALUES (2, 1, 'B', '16', 1);
INSERT INTO `question_options` VALUES (3, 1, 'C', '17', 0);
INSERT INTO `question_options` VALUES (4, 1, 'D', '15', 0);
INSERT INTO `question_options` VALUES (5, 2, 'A', 'O(n)', 0);
INSERT INTO `question_options` VALUES (6, 2, 'B', 'O(n²)', 0);
INSERT INTO `question_options` VALUES (7, 2, 'C', 'O(logn)', 1);
INSERT INTO `question_options` VALUES (8, 2, 'D', 'O(1)', 0);
INSERT INTO `question_options` VALUES (9, 3, 'A', '进程是资源分配的基本单位', 0);
INSERT INTO `question_options` VALUES (10, 3, 'B', '线程是CPU调度的基本单位', 0);
INSERT INTO `question_options` VALUES (11, 3, 'C', '同一进程的线程共享进程的资源', 0);
INSERT INTO `question_options` VALUES (12, 3, 'D', '线程之间不共享任何资源', 1);
INSERT INTO `question_options` VALUES (13, 4, 'A', '接收方通过调整窗口大小来控制发送方的发送速率', 1);
INSERT INTO `question_options` VALUES (14, 4, 'B', '窗口大小可以动态调整', 1);
INSERT INTO `question_options` VALUES (15, 4, 'C', '窗口大小只能是2的幂', 0);
INSERT INTO `question_options` VALUES (16, 4, 'D', '窗口大小为0时表示暂停发送', 1);
INSERT INTO `question_options` VALUES (17, 5, 'A', '指针变量存储的是内存地址', 0);
INSERT INTO `question_options` VALUES (18, 5, 'B', '指针的大小与系统位数有关', 0);
INSERT INTO `question_options` VALUES (19, 5, 'C', 'void指针可以直接解引用', 1);
INSERT INTO `question_options` VALUES (20, 5, 'D', '数组名是一个常量指针', 0);
INSERT INTO `question_options` VALUES (21, 7, 'A', 'const修饰的变量不能被修改', 1);
INSERT INTO `question_options` VALUES (22, 7, 'B', 'const可以修饰函数参数', 1);
INSERT INTO `question_options` VALUES (23, 7, 'C', 'const修饰的变量可以延迟初始化', 0);
INSERT INTO `question_options` VALUES (24, 7, 'D', 'const可以和指针一起使用', 1);
INSERT INTO `question_options` VALUES (25, 8, 'A', 'O(n)', 1);
INSERT INTO `question_options` VALUES (26, 8, 'B', 'O(n²)', 0);
INSERT INTO `question_options` VALUES (27, 8, 'C', 'O(logn)', 0);
INSERT INTO `question_options` VALUES (28, 8, 'D', 'O(1)', 0);
INSERT INTO `question_options` VALUES (29, 9, 'A', 'O(logn)', 0);
INSERT INTO `question_options` VALUES (30, 9, 'B', 'O(n)', 1);
INSERT INTO `question_options` VALUES (31, 9, 'C', 'O(1)', 0);
INSERT INTO `question_options` VALUES (32, 9, 'D', 'O(n²)', 0);
INSERT INTO `question_options` VALUES (33, 10, 'A', '开放定址法', 0);
INSERT INTO `question_options` VALUES (34, 10, 'B', '链地址法', 0);
INSERT INTO `question_options` VALUES (35, 10, 'C', '再散列法', 0);
INSERT INTO `question_options` VALUES (36, 10, 'D', '树形平衡', 1);
INSERT INTO `question_options` VALUES (37, 11, 'A', '流水线技术可以提高系统的吞吐率', 0);
INSERT INTO `question_options` VALUES (38, 11, 'B', '流水线越深，理论吞吐率越大', 0);
INSERT INTO `question_options` VALUES (39, 11, 'C', '流水线中可能存在数据相关', 0);
INSERT INTO `question_options` VALUES (40, 11, 'D', '流水线可以减少单条指令的执行时间', 1);
INSERT INTO `question_options` VALUES (41, 12, 'A', 'RISC的指令数量比CISC多', 1);
INSERT INTO `question_options` VALUES (42, 12, 'B', 'RISC的指令格式统一', 0);
INSERT INTO `question_options` VALUES (43, 12, 'C', 'RISC更多采用硬布线控制', 0);
INSERT INTO `question_options` VALUES (44, 12, 'D', 'RISC的指令执行时间基本相同', 0);
INSERT INTO `question_options` VALUES (45, 13, 'A', '先来先服务(FCFS)', 1);
INSERT INTO `question_options` VALUES (46, 13, 'B', '最短作业优先(SJF)', 0);
INSERT INTO `question_options` VALUES (47, 13, 'C', '优先级调度', 0);
INSERT INTO `question_options` VALUES (48, 13, 'D', '多级反馈队列', 0);
INSERT INTO `question_options` VALUES (49, 14, 'A', '死锁预防可能导致系统资源利用率低', 0);
INSERT INTO `question_options` VALUES (50, 14, 'B', '银行家算法是一种死锁避免算法', 0);
INSERT INTO `question_options` VALUES (51, 14, 'C', '死锁预防和死锁避免是相同的策略', 1);
INSERT INTO `question_options` VALUES (52, 14, 'D', '产生死锁的必要条件有四个', 0);
INSERT INTO `question_options` VALUES (53, 15, 'A', 'TCP是面向连接的协议', 0);
INSERT INTO `question_options` VALUES (54, 15, 'B', 'UDP是无连接的协议', 0);
INSERT INTO `question_options` VALUES (55, 15, 'C', 'UDP不提供差错检测功能', 1);
INSERT INTO `question_options` VALUES (56, 15, 'D', 'TCP提供可靠传输服务', 0);
INSERT INTO `question_options` VALUES (57, 16, 'A', 'HTTP是无状态协议', 1);
INSERT INTO `question_options` VALUES (58, 16, 'B', 'HTTP默认端口号是8080', 0);
INSERT INTO `question_options` VALUES (59, 16, 'C', 'HTTP/1.1支持持久连接', 1);
INSERT INTO `question_options` VALUES (60, 16, 'D', 'HTTP可以使用Cookie保持会话状态', 1);

-- ----------------------------
-- Table structure for questions
-- ----------------------------
DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '题目内容',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '题目类型：SINGLE-单选题，MULTIPLE-多选题，JUDGE-判断题',
  `answer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '正确答案',
  `analysis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '答案解析',
  `bank_id` bigint NOT NULL COMMENT '所属题库ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_at` datetime(6) NULL DEFAULT NULL,
  `options` json NULL,
  `point_id` bigint NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `category_id` bigint NULL DEFAULT NULL,
  `chapter` int NULL DEFAULT NULL,
  `sort_order` int NULL DEFAULT NULL,
  `difficulty` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_bank_id`(`bank_id` ASC) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `FKctl6tuf74n8cufkb3ulj6b3fc`(`category_id` ASC) USING BTREE,
  CONSTRAINT `FK54pk8ubikyxblrwho5ub41l5m` FOREIGN KEY (`category_id`) REFERENCES `bank_categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKctl6tuf74n8cufkb3ulj6b3fc` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `questions_ibfk_1` FOREIGN KEY (`bank_id`) REFERENCES `question_banks` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of questions
-- ----------------------------
INSERT INTO `questions` VALUES (1, '在一棵完全二叉树中，具有2个子女的结点数为15，则该二叉树的叶子结点数为()', 'SINGLE', 'B', '完全二叉树性质：若有n2个度为2的结点，则叶子结点数n0 = n2 + 1，所以叶子结点数为16', 1, '2024-11-14 22:42:48', '2024-11-14 22:42:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (2, '对于包含n个节点的二叉搜索树，查找、插入和删除操作的平均时间复杂度是()', 'SINGLE', 'C', '二叉搜索树的查找、插入和删除操作的平均时间复杂度都是O(logn)，最坏情况下为O(n)', 2, '2024-11-14 22:42:48', '2024-11-14 22:42:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (3, '下列关于进程和线程的说法中，错误的是()', 'SINGLE', 'D', '线程是CPU调度的基本单位，而进程是资源分配的基本单位。线程之间共享进程的资源。', 3, '2024-11-14 22:42:48', '2024-11-14 22:42:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (4, 'TCP协议使用滑动窗口机制实现流量控制，以下说法正确的是()', 'MULTIPLE', 'A,B,D', 'TCP的滑动窗口机制可以实现流量控制，防止发送方发送速率过快导致接收方无法处理。', 4, '2024-11-14 22:42:48', '2024-11-14 22:42:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (5, '以下关于指针的说法中，错误的是()', 'SINGLE', 'C', '指针是一种数据类型，用于存储内存地址。void指针可以指向任何类型的数据，但需要类型转换后才能解引用。', 5, '2024-11-14 22:42:48', '2024-11-14 22:42:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (6, '在C语言中，数组作为函数参数传递时，实际上传递的是数组的首地址。', 'JUDGE', 'TRUE', '在C语言中，数组作为参数传递时会退化为指针，传递的是数组的首地址，而不是整个数组的副本。', 5, '2024-11-14 22:42:48', '2024-11-14 22:42:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (7, '关于C语言中的const关键字，以下说法正确的有()', 'MULTIPLE', 'A,B,D', 'const可以修饰变量、指针和函数参数，用于表示其值不能被修改。const修饰的变量必须在声明时初始化。', 5, '2024-11-14 22:42:48', '2024-11-14 22:42:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (8, '在一个长度为n的顺序表中，删除所有值为x的元素的时间复杂度是', 'SINGLE', 'A', '需要遍历一遍数组，时间复杂度为O(n)', 1, '2024-11-15 19:39:56', '2024-11-15 19:39:56', NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (9, '对于包含n个结点的二叉树，其中序遍历的非递归算法中，栈的最大容量可能是', 'SINGLE', 'B', '最坏情况是一个只有左子树的二叉树，需要将所有节点入栈，空间复杂度为O(n)', 1, '2024-11-15 19:39:56', '2024-11-15 19:39:56', NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (10, '在散列表中，处理冲突的方法不包括', 'SINGLE', 'D', '树形平衡不是散列表处理冲突的方法，常见的方法有开放定址法和链地址法', 1, '2024-11-15 19:39:56', '2024-11-15 19:39:56', NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (11, '以下关于流水线的描述，错误的是', 'SINGLE', 'D', '流水线技术可以提高系统的吞吐率，但不能减少单条指令的执行时间', 1, '2024-11-15 19:39:56', '2024-11-15 19:39:56', NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (12, '关于RISC和CISC的区别，说法错误的是', 'SINGLE', 'A', 'RISC的指令数量通常比CISC少，而不是更多', 1, '2024-11-15 19:39:56', '2024-11-15 19:39:56', NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (13, '下列进程调度算法中，不会导致饥饿现象的是', 'SINGLE', 'A', '先来先服务(FCFS)调度算法按照进程到达的先后顺序进行调度，不会产生饥饿现象', 1, '2024-11-15 19:39:56', '2024-11-15 19:39:56', NULL, NULL, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (14, '关于死锁的说法，错误的是', 'SINGLE', 'C', '死锁的预防和避免是两种不同的处理策略，预防是破坏死锁的必要条件，避免是动态检测和预防死锁的发生', 1, '2024-11-15 19:39:56', '2024-11-15 19:39:56', NULL, NULL, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (15, '以下关于TCP和UDP的描述，错误的是', 'SINGLE', 'C', 'UDP也提供差错检测功能，但不提供差错恢复功能', 1, '2024-11-15 19:39:56', '2024-11-15 19:39:56', NULL, NULL, 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `questions` VALUES (16, '以下关于HTTP协议的描述，正确的是', 'MULTIPLE', 'A,C,D', 'HTTP是无状态协议，默认端口号是80，支持持久连接', 1, '2024-11-15 19:39:56', '2024-11-15 19:39:56', NULL, NULL, 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for user_answers
-- ----------------------------
DROP TABLE IF EXISTS `user_answers`;
CREATE TABLE `user_answers`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `question_id` bigint NOT NULL COMMENT '题目ID',
  `bank_id` bigint NOT NULL COMMENT '题库ID',
  `answer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户答案',
  `is_correct` tinyint(1) NOT NULL COMMENT '是否正确',
  `mode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '练习模式：random-随机练习，wrong-错题练习，collect-收藏练习',
  `practice_time` int NULL DEFAULT 0 COMMENT '做题用时(秒)',
  `review_count` int NULL DEFAULT 0 COMMENT '复习次数',
  `last_review_at` datetime NULL DEFAULT NULL COMMENT '最后复习时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '答题时间',
  `created_at` datetime(6) NULL DEFAULT NULL,
  `practice_mode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_question_id`(`question_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_user_question`(`user_id` ASC, `question_id` ASC) USING BTREE,
  INDEX `idx_bank_id`(`bank_id` ASC) USING BTREE,
  CONSTRAINT `fk_answers_bank` FOREIGN KEY (`bank_id`) REFERENCES `question_banks` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_answers_question` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_answers_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_answers_ibfk_2` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 104 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_answers
-- ----------------------------
INSERT INTO `user_answers` VALUES (1, 28, 1, 1, 'C', 0, 'sequence', 2, 0, NULL, '2024-11-22 21:35:06', '2024-11-22 13:35:06.675621', NULL, NULL);
INSERT INTO `user_answers` VALUES (2, 28, 8, 1, 'C', 0, 'sequence', 8, 0, NULL, '2024-11-22 21:35:12', '2024-11-22 13:35:12.923951', NULL, NULL);
INSERT INTO `user_answers` VALUES (3, 28, 9, 1, 'D', 0, 'sequence', 10, 0, NULL, '2024-11-22 21:35:15', '2024-11-22 13:35:15.115041', NULL, NULL);
INSERT INTO `user_answers` VALUES (4, 28, 10, 1, 'C', 0, 'sequence', 14, 0, NULL, '2024-11-22 21:35:19', '2024-11-22 13:35:19.232116', NULL, NULL);
INSERT INTO `user_answers` VALUES (5, 28, 11, 1, 'B', 0, 'sequence', 16, 0, NULL, '2024-11-22 21:35:21', '2024-11-22 13:35:21.225448', NULL, NULL);
INSERT INTO `user_answers` VALUES (6, 28, 12, 1, 'C', 0, 'sequence', 18, 0, NULL, '2024-11-22 21:35:22', '2024-11-22 13:35:22.727408', NULL, NULL);
INSERT INTO `user_answers` VALUES (7, 28, 13, 1, 'B', 0, 'sequence', 19, 0, NULL, '2024-11-22 21:35:24', '2024-11-22 13:35:24.397292', NULL, NULL);
INSERT INTO `user_answers` VALUES (8, 28, 14, 1, 'A', 0, 'sequence', 21, 0, NULL, '2024-11-22 21:35:26', '2024-11-22 13:35:26.190280', NULL, NULL);
INSERT INTO `user_answers` VALUES (9, 28, 15, 1, 'C', 1, 'sequence', 26, 0, NULL, '2024-11-22 21:35:30', '2024-11-22 13:35:30.678175', NULL, NULL);
INSERT INTO `user_answers` VALUES (10, 28, 16, 1, 'A,C', 0, 'sequence', 29, 0, NULL, '2024-11-22 21:35:33', '2024-11-22 13:35:33.869103', NULL, NULL);
INSERT INTO `user_answers` VALUES (11, 28, 2, 2, 'B', 0, 'sequence', 1, 0, NULL, '2024-11-22 21:35:58', '2024-11-22 13:35:58.743706', NULL, NULL);
INSERT INTO `user_answers` VALUES (12, 28, 2, 2, 'B', 0, 'sequence', 1, 0, NULL, '2024-11-22 21:36:04', '2024-11-22 13:36:04.837328', NULL, NULL);
INSERT INTO `user_answers` VALUES (13, 28, 1, 1, 'C', 0, 'sequence', 2, 0, NULL, '2024-11-22 21:38:27', '2024-11-22 13:38:27.195848', NULL, NULL);
INSERT INTO `user_answers` VALUES (14, 28, 8, 1, 'C', 0, 'sequence', 4, 0, NULL, '2024-11-22 21:38:29', '2024-11-22 13:38:29.257946', NULL, NULL);
INSERT INTO `user_answers` VALUES (15, 28, 9, 1, 'C', 0, 'sequence', 5, 0, NULL, '2024-11-22 21:38:30', '2024-11-22 13:38:30.512771', NULL, NULL);
INSERT INTO `user_answers` VALUES (16, 28, 10, 1, 'C', 0, 'sequence', 6, 0, NULL, '2024-11-22 21:38:31', '2024-11-22 13:38:31.987907', NULL, NULL);
INSERT INTO `user_answers` VALUES (17, 28, 11, 1, 'C', 0, 'sequence', 8, 0, NULL, '2024-11-22 21:38:33', '2024-11-22 13:38:33.656396', NULL, NULL);
INSERT INTO `user_answers` VALUES (18, 28, 12, 1, 'B', 0, 'sequence', 10, 0, NULL, '2024-11-22 21:38:35', '2024-11-22 13:38:35.252235', NULL, NULL);
INSERT INTO `user_answers` VALUES (19, 28, 13, 1, 'C', 0, 'sequence', 12, 0, NULL, '2024-11-22 21:38:37', '2024-11-22 13:38:37.960313', NULL, NULL);
INSERT INTO `user_answers` VALUES (20, 28, 14, 1, 'C', 1, 'sequence', 15, 0, NULL, '2024-11-22 21:38:40', '2024-11-22 13:38:40.184189', NULL, NULL);
INSERT INTO `user_answers` VALUES (21, 28, 15, 1, 'C', 1, 'sequence', 16, 0, NULL, '2024-11-22 21:38:41', '2024-11-22 13:38:41.479283', NULL, NULL);
INSERT INTO `user_answers` VALUES (22, 28, 16, 1, 'C', 0, 'sequence', 18, 0, NULL, '2024-11-22 21:38:43', '2024-11-22 13:38:43.507180', NULL, NULL);
INSERT INTO `user_answers` VALUES (23, 28, 2, 2, 'B', 0, 'sequence', 1, 0, NULL, '2024-11-22 21:40:22', '2024-11-22 13:40:22.593314', NULL, NULL);
INSERT INTO `user_answers` VALUES (24, 28, 2, 2, 'A', 0, 'sequence', 1, 0, NULL, '2024-11-22 21:40:39', '2024-11-22 13:40:39.744489', NULL, NULL);
INSERT INTO `user_answers` VALUES (25, 28, 1, 1, 'D', 0, 'sequence', 2, 0, NULL, '2024-11-22 21:42:11', '2024-11-22 13:42:11.045889', NULL, NULL);
INSERT INTO `user_answers` VALUES (26, 28, 2, 2, 'D', 0, 'sequence', 2, 0, NULL, '2024-11-22 21:42:20', '2024-11-22 13:42:20.061394', NULL, NULL);
INSERT INTO `user_answers` VALUES (27, 28, 1, 1, 'A', 0, 'sequence', 12, 0, NULL, '2024-11-22 21:45:03', '2024-11-22 13:45:03.089829', NULL, NULL);
INSERT INTO `user_answers` VALUES (28, 28, 8, 1, 'B', 0, 'sequence', 16, 0, NULL, '2024-11-22 21:45:07', '2024-11-22 13:45:07.194907', NULL, NULL);
INSERT INTO `user_answers` VALUES (29, 28, 9, 1, 'B', 1, 'sequence', 18, 0, NULL, '2024-11-22 21:45:09', '2024-11-22 13:45:09.722780', NULL, NULL);
INSERT INTO `user_answers` VALUES (30, 29, 12, 1, 'D', 0, 'sequence', 14, 0, NULL, '2024-11-22 21:47:01', '2024-11-22 13:47:01.449165', NULL, NULL);
INSERT INTO `user_answers` VALUES (31, 29, 11, 1, 'B', 0, 'sequence', 16, 0, NULL, '2024-11-22 21:47:03', '2024-11-22 13:47:03.338199', NULL, NULL);
INSERT INTO `user_answers` VALUES (32, 29, 10, 1, 'B', 0, 'sequence', 18, 0, NULL, '2024-11-22 21:47:05', '2024-11-22 13:47:05.125655', NULL, NULL);
INSERT INTO `user_answers` VALUES (33, 29, 9, 1, 'C', 0, 'sequence', 19, 0, NULL, '2024-11-22 21:47:06', '2024-11-22 13:47:06.150902', NULL, NULL);
INSERT INTO `user_answers` VALUES (34, 29, 8, 1, 'C', 0, 'sequence', 20, 0, NULL, '2024-11-22 21:47:07', '2024-11-22 13:47:07.082573', NULL, NULL);
INSERT INTO `user_answers` VALUES (35, 29, 1, 1, 'B', 1, 'sequence', 21, 0, NULL, '2024-11-22 21:47:08', '2024-11-22 13:47:08.572521', NULL, NULL);
INSERT INTO `user_answers` VALUES (36, 29, 13, 1, 'C', 0, 'sequence', 26, 0, NULL, '2024-11-22 21:47:12', '2024-11-22 13:47:12.923496', NULL, NULL);
INSERT INTO `user_answers` VALUES (37, 29, 14, 1, 'B', 0, 'sequence', 28, 0, NULL, '2024-11-22 21:47:15', '2024-11-22 13:47:15.470325', NULL, NULL);
INSERT INTO `user_answers` VALUES (38, 29, 15, 1, 'B', 0, 'sequence', 30, 0, NULL, '2024-11-22 21:47:17', '2024-11-22 13:47:17.470110', NULL, NULL);
INSERT INTO `user_answers` VALUES (39, 29, 16, 1, 'B', 0, 'sequence', 33, 0, NULL, '2024-11-22 21:47:19', '2024-11-22 13:47:19.870850', NULL, NULL);
INSERT INTO `user_answers` VALUES (40, 29, 1, 1, 'B', 1, 'sequence', 2, 0, NULL, '2024-11-22 21:48:18', '2024-11-22 13:48:18.718423', NULL, NULL);
INSERT INTO `user_answers` VALUES (41, 29, 8, 1, 'B', 0, 'sequence', 4, 0, NULL, '2024-11-22 21:48:20', '2024-11-22 13:48:20.339503', NULL, NULL);
INSERT INTO `user_answers` VALUES (42, 29, 9, 1, 'B', 1, 'sequence', 7, 0, NULL, '2024-11-22 21:48:23', '2024-11-22 13:48:23.082269', NULL, NULL);
INSERT INTO `user_answers` VALUES (43, 29, 1, 1, 'A', 0, 'sequence', 4, 0, NULL, '2024-11-22 21:49:30', '2024-11-22 13:49:30.400477', NULL, NULL);
INSERT INTO `user_answers` VALUES (44, 29, 8, 1, 'B', 0, 'sequence', 7, 0, NULL, '2024-11-22 21:49:33', '2024-11-22 13:49:33.857961', NULL, NULL);
INSERT INTO `user_answers` VALUES (45, 29, 9, 1, 'B', 1, 'sequence', 13, 0, NULL, '2024-11-22 21:49:39', '2024-11-22 13:49:39.612223', NULL, NULL);
INSERT INTO `user_answers` VALUES (46, 29, 10, 1, 'C', 0, 'sequence', 20, 0, NULL, '2024-11-22 21:49:46', '2024-11-22 13:49:46.883627', NULL, NULL);
INSERT INTO `user_answers` VALUES (47, 29, 9, 1, 'B', 1, 'sequence', 9, 0, NULL, '2024-11-22 21:50:54', '2024-11-22 13:50:54.750489', NULL, NULL);
INSERT INTO `user_answers` VALUES (48, 29, 10, 1, 'C', 0, 'sequence', 11, 0, NULL, '2024-11-22 21:50:56', '2024-11-22 13:50:56.758327', NULL, NULL);
INSERT INTO `user_answers` VALUES (49, 29, 12, 1, 'C', 0, 'sequence', 16, 0, NULL, '2024-11-22 21:51:01', '2024-11-22 13:51:01.763272', NULL, NULL);
INSERT INTO `user_answers` VALUES (50, 29, 13, 1, 'D', 0, 'sequence', 17, 0, NULL, '2024-11-22 21:51:03', '2024-11-22 13:51:03.582097', NULL, NULL);
INSERT INTO `user_answers` VALUES (51, 29, 14, 1, 'B', 0, 'sequence', 21, 0, NULL, '2024-11-22 21:51:07', '2024-11-22 13:51:07.052088', NULL, NULL);
INSERT INTO `user_answers` VALUES (52, 29, 15, 1, 'B', 0, 'sequence', 23, 0, NULL, '2024-11-22 21:51:09', '2024-11-22 13:51:09.278279', NULL, NULL);
INSERT INTO `user_answers` VALUES (53, 29, 16, 1, 'C', 0, 'sequence', 25, 0, NULL, '2024-11-22 21:51:11', '2024-11-22 13:51:11.541937', NULL, NULL);
INSERT INTO `user_answers` VALUES (54, 29, 1, 1, 'A', 0, 'sequence', 1, 0, NULL, '2024-11-22 21:51:25', '2024-11-22 13:51:25.662184', NULL, NULL);
INSERT INTO `user_answers` VALUES (55, 29, 8, 1, 'B', 0, 'sequence', 4, 0, NULL, '2024-11-22 21:51:27', '2024-11-22 13:51:27.971447', NULL, NULL);
INSERT INTO `user_answers` VALUES (56, 29, 9, 1, 'C', 0, 'sequence', 6, 0, NULL, '2024-11-22 21:51:29', '2024-11-22 13:51:29.805496', NULL, NULL);
INSERT INTO `user_answers` VALUES (57, 29, 2, 2, 'D', 0, 'sequence', 2, 0, NULL, '2024-11-22 21:51:34', '2024-11-22 13:51:34.744401', NULL, NULL);
INSERT INTO `user_answers` VALUES (58, 29, 2, 2, 'B', 0, 'sequence', 1, 0, NULL, '2024-11-22 21:54:15', '2024-11-22 13:54:15.777514', NULL, NULL);
INSERT INTO `user_answers` VALUES (59, 29, 1, 1, 'A', 0, 'sequence', 2, 0, NULL, '2024-11-22 21:58:25', '2024-11-22 13:58:25.384636', NULL, NULL);
INSERT INTO `user_answers` VALUES (60, 29, 8, 1, 'D', 0, 'sequence', 15, 0, NULL, '2024-11-22 21:58:38', '2024-11-22 13:58:38.180834', NULL, NULL);
INSERT INTO `user_answers` VALUES (61, 29, 2, 2, 'B', 0, 'sequence', 3, 0, NULL, '2024-11-23 09:47:23', '2024-11-23 01:47:23.661468', NULL, NULL);
INSERT INTO `user_answers` VALUES (62, 29, 2, 2, 'B', 0, 'sequence', 2, 0, NULL, '2024-11-23 09:47:33', '2024-11-23 01:47:33.392517', NULL, NULL);
INSERT INTO `user_answers` VALUES (63, 29, 1, 1, 'B', 1, 'sequence', 1, 0, NULL, '2024-11-23 09:47:45', '2024-11-23 01:47:45.385279', NULL, NULL);
INSERT INTO `user_answers` VALUES (64, 29, 8, 1, 'B', 0, 'sequence', 3, 0, NULL, '2024-11-23 09:47:46', '2024-11-23 01:47:46.842611', NULL, NULL);
INSERT INTO `user_answers` VALUES (65, 29, 9, 1, 'B', 1, 'sequence', 5, 0, NULL, '2024-11-23 09:47:49', '2024-11-23 01:47:49.534593', NULL, NULL);
INSERT INTO `user_answers` VALUES (66, 29, 10, 1, 'C', 0, 'sequence', 7, 0, NULL, '2024-11-23 09:47:51', '2024-11-23 01:47:51.223294', NULL, NULL);
INSERT INTO `user_answers` VALUES (67, 29, 11, 1, 'C', 0, 'sequence', 10, 0, NULL, '2024-11-23 09:47:54', '2024-11-23 01:47:54.087719', NULL, NULL);
INSERT INTO `user_answers` VALUES (68, 29, 12, 1, 'C', 0, 'sequence', 12, 0, NULL, '2024-11-23 09:47:56', '2024-11-23 01:47:56.243061', NULL, NULL);
INSERT INTO `user_answers` VALUES (69, 29, 13, 1, 'C', 0, 'sequence', 15, 0, NULL, '2024-11-23 09:47:59', '2024-11-23 01:47:59.254862', NULL, NULL);
INSERT INTO `user_answers` VALUES (70, 29, 14, 1, 'B', 0, 'sequence', 18, 0, NULL, '2024-11-23 09:48:01', '2024-11-23 01:48:01.913646', NULL, NULL);
INSERT INTO `user_answers` VALUES (71, 29, 15, 1, 'C', 1, 'sequence', 21, 0, NULL, '2024-11-23 09:48:04', '2024-11-23 01:48:04.828736', NULL, NULL);
INSERT INTO `user_answers` VALUES (72, 29, 16, 1, 'B', 0, 'sequence', 22, 0, NULL, '2024-11-23 09:48:06', '2024-11-23 01:48:06.695045', NULL, NULL);
INSERT INTO `user_answers` VALUES (73, 29, 1, 1, 'B', 1, 'sequence', 2, 0, NULL, '2024-11-23 09:50:36', '2024-11-23 01:50:36.715516', NULL, NULL);
INSERT INTO `user_answers` VALUES (74, 29, 8, 1, 'C', 0, 'sequence', 4, 0, NULL, '2024-11-23 09:50:38', '2024-11-23 01:50:38.554547', NULL, NULL);
INSERT INTO `user_answers` VALUES (75, 29, 9, 1, 'A', 0, 'sequence', 6, 0, NULL, '2024-11-23 09:50:40', '2024-11-23 01:50:40.598414', NULL, NULL);
INSERT INTO `user_answers` VALUES (76, 29, 10, 1, 'B', 0, 'sequence', 9, 0, NULL, '2024-11-23 09:50:42', '2024-11-23 01:50:42.767622', NULL, NULL);
INSERT INTO `user_answers` VALUES (77, 29, 11, 1, 'C', 0, 'sequence', 11, 0, NULL, '2024-11-23 09:50:45', '2024-11-23 01:50:45.340434', NULL, NULL);
INSERT INTO `user_answers` VALUES (78, 29, 12, 1, 'B', 0, 'sequence', 14, 0, NULL, '2024-11-23 09:50:48', '2024-11-23 01:50:48.023845', NULL, NULL);
INSERT INTO `user_answers` VALUES (79, 29, 13, 1, 'C', 0, 'sequence', 16, 0, NULL, '2024-11-23 09:50:50', '2024-11-23 01:50:50.215344', NULL, NULL);
INSERT INTO `user_answers` VALUES (80, 29, 14, 1, 'D', 0, 'sequence', 18, 0, NULL, '2024-11-23 09:50:52', '2024-11-23 01:50:52.709732', NULL, NULL);
INSERT INTO `user_answers` VALUES (81, 29, 15, 1, 'C', 1, 'sequence', 22, 0, NULL, '2024-11-23 09:50:55', '2024-11-23 01:50:55.917761', NULL, NULL);
INSERT INTO `user_answers` VALUES (82, 29, 16, 1, 'B', 0, 'sequence', 24, 0, NULL, '2024-11-23 09:50:58', '2024-11-23 01:50:58.562099', NULL, NULL);
INSERT INTO `user_answers` VALUES (83, 29, 2, 2, 'D', 0, 'sequence', 2, 0, NULL, '2024-11-23 09:52:34', '2024-11-23 01:52:34.269583', NULL, NULL);
INSERT INTO `user_answers` VALUES (84, 29, 2, 2, 'B', 0, 'sequence', 1, 0, NULL, '2024-11-23 09:54:27', '2024-11-23 01:54:27.063391', NULL, NULL);
INSERT INTO `user_answers` VALUES (85, 29, 2, 2, 'C', 1, 'sequence', 2, 0, NULL, '2024-11-23 09:54:36', '2024-11-23 01:54:36.981324', NULL, NULL);
INSERT INTO `user_answers` VALUES (86, 29, 2, 2, 'D', 0, 'sequence', 2, 0, NULL, '2024-11-23 09:54:49', '2024-11-23 01:54:49.942679', NULL, NULL);
INSERT INTO `user_answers` VALUES (87, 29, 2, 2, 'C', 1, 'sequence', 4, 0, NULL, '2024-11-23 09:54:59', '2024-11-23 01:54:59.407816', NULL, NULL);
INSERT INTO `user_answers` VALUES (88, 29, 2, 2, 'C', 1, 'sequence', 2, 0, NULL, '2024-11-23 09:56:15', '2024-11-23 01:56:15.856025', NULL, NULL);
INSERT INTO `user_answers` VALUES (89, 29, 2, 2, 'D', 0, 'sequence', 66, 0, NULL, '2024-11-23 09:59:00', '2024-11-23 01:59:00.221777', NULL, NULL);
INSERT INTO `user_answers` VALUES (90, 29, 1, 1, 'C', 0, 'sequence', 1, 0, NULL, '2024-11-23 09:59:16', '2024-11-23 01:59:16.611037', NULL, NULL);
INSERT INTO `user_answers` VALUES (91, 29, 8, 1, 'C', 0, 'sequence', 5, 0, NULL, '2024-11-23 09:59:20', '2024-11-23 01:59:20.230895', NULL, NULL);
INSERT INTO `user_answers` VALUES (92, 29, 9, 1, 'B', 1, 'sequence', 7, 0, NULL, '2024-11-23 09:59:22', '2024-11-23 01:59:22.225328', NULL, NULL);
INSERT INTO `user_answers` VALUES (93, 29, 10, 1, 'D', 1, 'sequence', 8, 0, NULL, '2024-11-23 09:59:23', '2024-11-23 01:59:23.826463', NULL, NULL);
INSERT INTO `user_answers` VALUES (94, 29, 11, 1, 'C', 0, 'sequence', 10, 0, NULL, '2024-11-23 09:59:25', '2024-11-23 01:59:25.467836', NULL, NULL);
INSERT INTO `user_answers` VALUES (95, 29, 12, 1, 'D', 0, 'sequence', 13, 0, NULL, '2024-11-23 09:59:27', '2024-11-23 01:59:27.918913', NULL, NULL);
INSERT INTO `user_answers` VALUES (96, 29, 13, 1, 'C', 0, 'sequence', 15, 0, NULL, '2024-11-23 09:59:29', '2024-11-23 01:59:29.942137', NULL, NULL);
INSERT INTO `user_answers` VALUES (97, 29, 14, 1, 'C', 1, 'sequence', 17, 0, NULL, '2024-11-23 09:59:32', '2024-11-23 01:59:32.731485', NULL, NULL);
INSERT INTO `user_answers` VALUES (98, 29, 15, 1, 'C', 1, 'sequence', 20, 0, NULL, '2024-11-23 09:59:35', '2024-11-23 01:59:35.331188', NULL, NULL);
INSERT INTO `user_answers` VALUES (99, 29, 16, 1, 'D', 0, 'sequence', 22, 0, NULL, '2024-11-23 09:59:37', '2024-11-23 01:59:37.187912', NULL, NULL);
INSERT INTO `user_answers` VALUES (100, 29, 2, 2, 'D', 0, 'sequence', 2, 0, NULL, '2024-11-23 10:02:31', '2024-11-23 02:02:31.253077', NULL, NULL);
INSERT INTO `user_answers` VALUES (101, 28, 2, 2, 'D', 0, 'sequence', 2, 0, NULL, '2024-11-23 10:11:19', '2024-11-23 02:11:19.460210', NULL, NULL);
INSERT INTO `user_answers` VALUES (102, 28, 2, 2, 'C', 1, 'sequence', 3, 0, NULL, '2024-11-23 11:23:57', '2024-11-23 03:23:57.326890', NULL, NULL);
INSERT INTO `user_answers` VALUES (103, 28, 3, 3, 'B', 0, 'sequence', 3, 0, NULL, '2024-11-23 11:24:08', '2024-11-23 03:24:08.369539', NULL, NULL);

-- ----------------------------
-- Table structure for user_collections
-- ----------------------------
DROP TABLE IF EXISTS `user_collections`;
CREATE TABLE `user_collections`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `question_id` bigint NOT NULL COMMENT '题目ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_question`(`user_id` ASC, `question_id` ASC) USING BTREE COMMENT '同一用户不能重复收藏同一题目',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_question_id`(`question_id` ASC) USING BTREE,
  CONSTRAINT `fk_collections_question` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_collections_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户题目收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_collections
-- ----------------------------
INSERT INTO `user_collections` VALUES (28, 29, 1, '2024-11-22 13:58:36');
INSERT INTO `user_collections` VALUES (29, 29, 8, '2024-11-22 13:58:39');

-- ----------------------------
-- Table structure for user_settings
-- ----------------------------
DROP TABLE IF EXISTS `user_settings`;
CREATE TABLE `user_settings`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `sound_enabled` tinyint(1) NULL DEFAULT 1,
  `vibration_enabled` tinyint(1) NULL DEFAULT 1,
  `default_question_count` int NULL DEFAULT 10,
  `daily_reminder` tinyint(1) NULL DEFAULT 0,
  `reminder_time` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '20:00',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_settings_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_user_settings_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_settings
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `gender` int NULL DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `open_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_r43af9ap4edm43mmtq01oddj6`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (28, NULL, '2024-11-18 13:43:04.505061', NULL, '1', NULL, '$2a$10$6I6l.asntVr0RTrKSVrwP.Z7iS7A8GdVxygNIqa8fuHjdmKEMorHi', '2024-11-18 13:43:04.505061', '1', NULL, NULL);
INSERT INTO `users` VALUES (29, NULL, '2024-11-22 13:46:30.635575', NULL, '2', NULL, '$2a$10$O7JUZt0z1xk3fUsC2B1cauwjKut6EPHs/LuTy66FbNi2hEwdffaeq', '2024-11-22 13:46:30.635575', '2', NULL, NULL);

-- ----------------------------
-- Table structure for verify_codes
-- ----------------------------
DROP TABLE IF EXISTS `verify_codes`;
CREATE TABLE `verify_codes`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `expire_time` datetime(6) NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `used` bit(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of verify_codes
-- ----------------------------

-- ----------------------------
-- Table structure for wrong_questions
-- ----------------------------
DROP TABLE IF EXISTS `wrong_questions`;
CREATE TABLE `wrong_questions`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `question_id` bigint NOT NULL,
  `review_count` int NULL DEFAULT 0,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'pending',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `last_review_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_question_id`(`question_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  CONSTRAINT `fk_wrong_question` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_wrong_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of wrong_questions
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
