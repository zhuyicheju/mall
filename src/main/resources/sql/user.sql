CREATE DATABASE IF NOT EXISTS myself_mall;

USE myself_mall;

CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户唯一标识',
    `username` varchar(50) NOT NULL COMMENT '登录名',
    `password` varchar(100) NOT NULL COMMENT '存放加密后的密码（不可明文）',
    `nickname` varchar(50) NULL COMMENT '用户昵称',
    `phone` varchar(20) NULL COMMENT '手机号（可选，后期做登录用）',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0禁用，1启用）',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间', -- 这里去掉重复的 CURRENT
    `last_login_time` datetime NULL COMMENT '最近登录时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_username` (`username`)
) COMMENT '用户表';