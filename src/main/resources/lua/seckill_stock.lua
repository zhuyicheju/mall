-- KEYS[1] = 库存 Key
-- KEYS[2] = 已购用户集合 Key
-- ARGV[1] = 用户ID

local stockKey = KEYS[1]
local userKey = KEYS[2]
local userId = ARGV[1]

-- ① 防重复购买
if redis.call("SISMEMBER", userKey, userId) == 1 then
    return 2  -- 重复购买
end

-- ② 扣减库存（先判断再扣，避免库存被扣成负数）
local currentStock = tonumber(redis.call("GET", stockKey))

if not currentStock then
    return 0  -- 库存 key 不存在，视为无库存
end

if currentStock <= 0 then
    return 0  -- 库存不足
end

-- 原子扣减库存
redis.call("DECR", stockKey)

-- ③ 记录购买行为
redis.call("SADD", userKey, userId)

return 1  -- 成功
