/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : free-fs

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2021-03-19 14:16:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `file_info`
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL COMMENT '资源路径',
  `name` varchar(128) DEFAULT NULL COMMENT '资源原始名称',
  `file_name` varchar(128) DEFAULT NULL COMMENT '资源名称',
  `suffix` varchar(20) DEFAULT NULL COMMENT '后缀名',
  `is_img` tinyint(1) DEFAULT NULL COMMENT '是否图片',
  `size` int(11) DEFAULT NULL COMMENT '尺寸',
  `type` varchar(10) DEFAULT NULL COMMENT '文件展示类型，非后缀名',
  `put_time` datetime DEFAULT NULL COMMENT '上传时间',
  `is_dir` tinyint(1) DEFAULT NULL COMMENT '是否目录',
  `parent_id` int(11) DEFAULT NULL,
  `source` varchar(10) DEFAULT NULL COMMENT '来源',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='文件资源表';

-- ----------------------------
-- Records of file_info
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_auth`
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth`;
CREATE TABLE `sys_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auth_code` varchar(30) DEFAULT NULL,
  `auth_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- ----------------------------
-- Records of sys_auth
-- ----------------------------
INSERT INTO `sys_auth` VALUES ('1', 'file:view', '查看文件');
INSERT INTO `sys_auth` VALUES ('2', 'file:copy', '拷贝文件');
INSERT INTO `sys_auth` VALUES ('3', 'file:move', '移动文件');
INSERT INTO `sys_auth` VALUES ('4', 'file:rename', '重命名文件');
INSERT INTO `sys_auth` VALUES ('5', 'file:download', '下载文件');
INSERT INTO `sys_auth` VALUES ('6', 'file:delete', '删除文件');
INSERT INTO `sys_auth` VALUES ('7', 'file:upload', '上传文件');
INSERT INTO `sys_auth` VALUES ('8', 'dir:manage', '目录管理');
INSERT INTO `sys_auth` VALUES ('9', 'dir:add', '创建目录');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_code` varchar(20) DEFAULT NULL COMMENT '角色标识',
  `role_name` varchar(20) DEFAULT NULL COMMENT '角色名称',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'admin', '超级管理员', '2021-03-12 13:51:11');
INSERT INTO `sys_role` VALUES ('2', 'user', '普通用户', '2021-03-12 13:54:15');

-- ----------------------------
-- Table structure for `sys_role_auth`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_auth`;
CREATE TABLE `sys_role_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `auth_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- ----------------------------
-- Records of sys_role_auth
-- ----------------------------
INSERT INTO `sys_role_auth` VALUES ('1', '1', '1');
INSERT INTO `sys_role_auth` VALUES ('2', '1', '2');
INSERT INTO `sys_role_auth` VALUES ('3', '1', '3');
INSERT INTO `sys_role_auth` VALUES ('4', '1', '4');
INSERT INTO `sys_role_auth` VALUES ('5', '1', '5');
INSERT INTO `sys_role_auth` VALUES ('6', '1', '6');
INSERT INTO `sys_role_auth` VALUES ('7', '1', '7');
INSERT INTO `sys_role_auth` VALUES ('8', '1', '8');
INSERT INTO `sys_role_auth` VALUES ('9', '1', '9');
INSERT INTO `sys_role_auth` VALUES ('10', '2', '1');
INSERT INTO `sys_role_auth` VALUES ('11', '2', '2');
INSERT INTO `sys_role_auth` VALUES ('12', '2', '5');
INSERT INTO `sys_role_auth` VALUES ('13', '2', '7');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(128) DEFAULT NULL COMMENT '用户名',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(128) DEFAULT NULL COMMENT '昵称',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', 'c4d3076e93a50605d40d6ef0099e5b52', '管理员', '2021-03-12 13:50:51');
INSERT INTO `sys_user` VALUES ('2', 'fs', '23fd15d38efcae8fc9f32f8a6f8c21a5', '普通用户', '2021-03-12 13:56:23');

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '1');
INSERT INTO `sys_user_role` VALUES ('2', '2', '2');
