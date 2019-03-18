/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:3306
 Source Schema         : graduation

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 18/03/2019 18:02:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for crawler_toutiao
-- ----------------------------
DROP TABLE IF EXISTS `crawler_toutiao`;
CREATE TABLE `crawler_toutiao`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章标题',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章简介',
  `connect_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '跳转链接',
  `datachange_createtime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `key_word` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关键词',
  `classify` tinyint(4) NULL DEFAULT NULL COMMENT '分类',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '逻辑删除',
  `information` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '信息',
  `hot_degree` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '热度',
  `img_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片链接',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of crawler_toutiao
-- ----------------------------
INSERT INTO `crawler_toutiao` VALUES (1, '', NULL, '', '2019-03-11 17:47:35', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (2, '', NULL, '', '2019-03-11 17:47:35', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (3, '', NULL, '', '2019-03-11 17:47:35', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (4, '', NULL, '', '2019-03-11 17:47:35', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (5, '', NULL, '', '2019-03-11 17:47:35', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (6, '', NULL, '', '2019-03-11 17:47:36', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (7, '', NULL, '', '2019-03-11 17:47:36', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (8, '', NULL, '', '2019-03-11 17:47:36', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (9, '', NULL, '', '2019-03-11 17:47:36', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (10, '', NULL, '', '2019-03-11 17:47:36', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (11, '', NULL, '', '2019-03-11 17:47:37', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (12, '', NULL, '', '2019-03-11 17:47:37', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (13, '', NULL, '', '2019-03-11 17:47:37', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (14, '', NULL, '', '2019-03-11 17:47:37', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (15, '', NULL, '', '2019-03-11 17:47:37', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (16, '', NULL, '', '2019-03-11 17:47:37', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_toutiao` VALUES (17, '头条测试', NULL, '', '2019-03-18 18:00:29', NULL, NULL, 1, '{\"author\":\"test\",\"comment\":\"1k\",\"like\":\"10k\",\"reading\":\"100\"}', 0, 'http://pan.xici.com/group4/M00/D2/60/rBABp1kSZAuEUKBXAAAAADI7p3A972.jpg/1010');

-- ----------------------------
-- Table structure for crawler_weibo
-- ----------------------------
DROP TABLE IF EXISTS `crawler_weibo`;
CREATE TABLE `crawler_weibo`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章标题',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章简介',
  `connect_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '跳转链接',
  `datachange_createtime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `key_word` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关键词',
  `classify` tinyint(4) NULL DEFAULT NULL COMMENT '分类',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '逻辑删除',
  `information` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '信息',
  `hot_degree` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '热度',
  `img_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片链接',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of crawler_weibo
-- ----------------------------
INSERT INTO `crawler_weibo` VALUES (1, 'lhf-测试', '该数据为测试数据', 'localhost/8080', '2019-03-17 14:58:45', 'test', 1, 1, '{\"author\":\"test\",\"comment\":\"1k\",\"like\":\"10k\",\"reading\":\"100\"}', 0, 'http://pic44.nipic.com/20140723/19276212_171901262000_2.jpg');
INSERT INTO `crawler_weibo` VALUES (2, '', NULL, '', '2019-03-11 17:10:05', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_weibo` VALUES (3, '', NULL, '', '2019-03-11 17:10:05', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_weibo` VALUES (4, '', NULL, '', '2019-03-11 17:10:05', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_weibo` VALUES (5, '', NULL, '', '2019-03-11 17:10:05', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_weibo` VALUES (6, '', NULL, '', '2019-03-11 17:10:06', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_weibo` VALUES (7, '', NULL, '', '2019-03-11 17:10:06', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_weibo` VALUES (8, '', NULL, '', '2019-03-11 17:10:06', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_weibo` VALUES (9, '', NULL, '', '2019-03-11 17:10:06', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_weibo` VALUES (10, '', NULL, '', '2019-03-11 17:10:06', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_weibo` VALUES (11, '', NULL, '', '2019-03-11 17:10:06', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_weibo` VALUES (12, '', NULL, '', '2019-03-11 17:10:07', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_weibo` VALUES (13, '', NULL, '', '2019-03-11 17:10:07', NULL, NULL, 1, NULL, 0, NULL);
INSERT INTO `crawler_weibo` VALUES (14, '', NULL, '', '2019-03-11 17:10:09', NULL, NULL, 1, NULL, 0, NULL);

SET FOREIGN_KEY_CHECKS = 1;
