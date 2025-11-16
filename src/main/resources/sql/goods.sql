CREATE DATABASE IF NOT EXISTS myself_mall;

USE myself_mall;

CREATE TABLE `goods`(
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(50) NOT NULL COMMENT '商品名称',
    `title` varchar(50) NOT NULL COMMENT '商品标题',
    `price` DECIMAL(10, 2) NULL NULL COMMENT '商品单价',
    `stock` bigint NOT NULL COMMENT '库存数',
    `description` varchar(255) COMMENT '商品描述',
    `image_url` varchar(255) NOT NULL COMMENT '商品头像',
    `create_time` DATETIME NOT NULL,
    `update_time` DATETIME NOT NULL,

    PRIMARY KEY (`id`)
) COMMENT '商品表'