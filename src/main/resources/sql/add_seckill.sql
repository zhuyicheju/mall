
ALTER TABLE product
ADD COLUMN `seckill_start_time` DATETIME NULL COMMENT '秒杀开始时间' AFTER `create_time`,
ADD COLUMN `seckill_end_time` DATETIME NULL COMMENT '秒杀结束时间' AFTER `seckill_start_time`,
ADD COLUMN `seckill_limit` INT DEFAULT 1 COMMENT '单用户限购数，默认1' AFTER `seckill_end_time`,
ADD COLUMN `is_seckill` TINYINT DEFAULT 1 COMMENT '是否秒杀商品，1=是 0=否' AFTER `seckill_limit`;