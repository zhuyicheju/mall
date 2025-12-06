键类型	键名格式	数据类型	存储值示例	预热时机	备注
秒杀库存（核心）	seckill:stock:{goodsId}	String	100（商品 1001 的库存）	活动前预热	库存计数器，Lua 扣减用
秒杀配置（核心）	seckill:config:{goodsId}	Hash	startTime:1743888000（时间戳）
endTime:1743891600
limit:1（单用户限购数）	活动前预热	判断秒杀时间 / 限购用
用户限购标记（核心）	seckill:user:{goodsId}:{userId}	String	1（已秒杀）	秒杀时 Lua 生成（无需预热）	标记用户是否已买，避免重复
秒杀开关（可选）	seckill:status:{goodsId}	String	1（开启）/0（关闭）	活动前预热