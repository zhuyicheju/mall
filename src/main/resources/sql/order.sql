CREATE TABLE `seckill_order` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `good_id` bigint NOT NULL COMMENT '商品ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    
    UNIQUE KEY `uk_user_goods` (`user_id`, `goods_id`)
) COMMENT '秒杀订单表';