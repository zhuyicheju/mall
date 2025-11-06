-- id	BIGINT	主键、自增	商品唯一ID	用Long避免高并发自增问题
-- name	VARCHAR(100)	NOT NULL	商品名称	列表展示主要字段
-- title	VARCHAR(255)	NULL	商品副标题	可选，用于宣传文案
-- price	DECIMAL(10,2)	NOT NULL	商品单价	用decimal避免浮点误差
-- stock	INT	NOT NULL DEFAULT 0	库存数量	后期会独立缓存
-- description	TEXT	NULL	商品详细描述	商品详情页展示
-- image_url	VARCHAR(255)	NULL	商品主图	可展示缩略图
-- status	TINYINT	NOT NULL DEFAULT 1	商品状态（1上架，0下架）	方便列表筛选
-- create_time	DATETIME	DEFAULT CURRENT_TIMESTAMP	创建时间	用于排序
-- update_time	DATETIME	DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP	更新时间	用于数据更新

CREATE DATABASE IF NOT EXISTS myself_mall;

USE myself_mall;

CREATE TABLE `goods`(
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(50) NOT NULL COMMENT '商品名称',
    `title` varchar(50) NOT NULL COMMENT '商品标题',
    `price` DECIMAL(10, 2) NULL NULL COMMENT '商品单价',
    'stock' bigint NOT NULL COMMENT '库存数',
    'description' varchar(255) COMMENT '商品描述',
    'image_url' varchar(255) NOT NULL COMMENT '商品头像',
    'create_time' DATETIME NOT NULL,
    'update_time' DATETIME NOT NULL,

    PRIMARY KEY (`id`),
    UN
) COMMENT '商品表'