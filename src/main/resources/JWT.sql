/*
Navicat MySQL Data Transfer

Source Server         : vita-vm
Source Server Version : 80015
Source Host           : 192.168.226.130:3306
Source Database       : JWT

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2019-03-01 16:11:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `name` varchar(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'ROLE_USER');
INSERT INTO `role` VALUES ('2', 'ROLE_ADMIN');
INSERT INTO `role` VALUES ('3', 'ROLE_AUTHOR');

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('6', '2');
INSERT INTO `sys_user_role` VALUES ('7', '2');
INSERT INTO `sys_user_role` VALUES ('9', '1');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(11) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `token` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('6', 'user', '$2a$10$kkATi54Mbt0hz.j0tU4Jd.RSsgoZGD6QKDI0QgoX8ku7YIyNJAaOu', null);
INSERT INTO `user` VALUES ('7', 'chen', '$2a$10$kfjGwbxNGeXRWmW7.X1m5OhU0LL59Mo/EfQxUPTXhYtA5fe3fZg5u', null);
INSERT INTO `user` VALUES ('9', 'Vita', '$2a$10$aCjvyXBUupdDHo4EjTPiueQB0ghhCpXLqBh3ADaE6HW6Y7Hl6BbYy', 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJWaXRhIiwiY3JlYXRlZCI6MTU1MTQyNzE1Njc3NiwiZXhwIjoxNTUxNDI3MjA2fQ.17UT-8YEtJOZIDtrGHR3eXdo0LvOAJfRXHZm7O8-Lyim1c0-FQl11tyU-3yXMApqDYZxXhP5VyBnod5mmcsqPQ');
