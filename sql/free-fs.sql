/*
 Navicat Premium Data Transfer

 Source Server         :
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           :
 Source Schema         : free-fs

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 03/07/2023 14:38:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info`  (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源路径',
                              `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源原始名称',
                              `file_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源名称',
                              `suffix` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后缀名',
                              `is_img` tinyint(1) NULL DEFAULT NULL COMMENT '是否图片',
                              `size` int NULL DEFAULT NULL COMMENT '尺寸',
                              `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件展示类型，非后缀名',
                              `put_time` datetime NULL DEFAULT NULL COMMENT '上传时间',
                              `is_dir` tinyint(1) NULL DEFAULT NULL COMMENT '是否目录',
                              `parent_id` int NULL DEFAULT NULL,
                              `user_id` int NULL DEFAULT NULL COMMENT '用户id',
                              `source` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件资源表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `permission_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                   `permission_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, 'file:view', '查看文件');
INSERT INTO `sys_permission` VALUES (2, 'file:copy', '拷贝文件');
INSERT INTO `sys_permission` VALUES (3, 'file:move', '移动文件');
INSERT INTO `sys_permission` VALUES (4, 'file:rename', '重命名文件');
INSERT INTO `sys_permission` VALUES (5, 'file:download', '下载文件');
INSERT INTO `sys_permission` VALUES (6, 'file:delete', '删除文件');
INSERT INTO `sys_permission` VALUES (7, 'file:upload', '上传文件');
INSERT INTO `sys_permission` VALUES (8, 'dir:manage', '目录管理');
INSERT INTO `sys_permission` VALUES (9, 'dir:add', '创建目录');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `role_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色标识',
                             `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
                             `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'admin', '超级管理员', '2021-03-12 13:51:11');
INSERT INTO `sys_role` VALUES (2, 'user', '普通用户', '2021-03-12 13:54:15');
INSERT INTO `sys_role` VALUES (3, 'test', '测试权限角色', '2023-06-30 02:47:32');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
                                        `id` int NOT NULL AUTO_INCREMENT,
                                        `role_id` int NULL DEFAULT NULL,
                                        `permission_id` int NULL DEFAULT NULL,
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 1, 1);
INSERT INTO `sys_role_permission` VALUES (2, 1, 2);
INSERT INTO `sys_role_permission` VALUES (3, 1, 3);
INSERT INTO `sys_role_permission` VALUES (4, 1, 4);
INSERT INTO `sys_role_permission` VALUES (5, 1, 5);
INSERT INTO `sys_role_permission` VALUES (6, 1, 6);
INSERT INTO `sys_role_permission` VALUES (7, 1, 7);
INSERT INTO `sys_role_permission` VALUES (8, 1, 8);
INSERT INTO `sys_role_permission` VALUES (9, 1, 9);
INSERT INTO `sys_role_permission` VALUES (10, 2, 1);
INSERT INTO `sys_role_permission` VALUES (11, 2, 2);
INSERT INTO `sys_role_permission` VALUES (12, 2, 3);
INSERT INTO `sys_role_permission` VALUES (13, 2, 4);
INSERT INTO `sys_role_permission` VALUES (14, 2, 5);
INSERT INTO `sys_role_permission` VALUES (15, 2, 6);
INSERT INTO `sys_role_permission` VALUES (16, 2, 7);
INSERT INTO `sys_role_permission` VALUES (17, 2, 8);
INSERT INTO `sys_role_permission` VALUES (18, 2, 9);
INSERT INTO `sys_role_permission` VALUES (19, 3, 1);
INSERT INTO `sys_role_permission` VALUES (20, 3, 2);
INSERT INTO `sys_role_permission` VALUES (21, 3, 7);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
                             `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
                             `nickname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
                             `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
                             `uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '唯一uuid',
                             `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '管理员', NULL, NULL, '2021-03-12 13:50:51');
INSERT INTO `sys_user` VALUES (2, 'fs', 'dce7cce055566bed799f788cd0048e209a27a473c0f48b956fa1f1780e80d2c1', '普通用户', NULL, NULL, '2021-03-12 13:56:23');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `user_id` int NULL DEFAULT NULL,
                                  `role_id` int NULL DEFAULT NULL,
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2, 2);

SET FOREIGN_KEY_CHECKS = 1;
