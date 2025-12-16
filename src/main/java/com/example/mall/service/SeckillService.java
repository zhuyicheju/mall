package com.example.mall.service;

import java.util.Set;

public interface SeckillService {
    public void preheatGood(Long id);
    public void preheatGoods(Set<Long> id);
    public void preheatAllSeckillGoods();
    public Set<Long> getSeckillGoods();
    public void doSeckill(Long userId, Long goodId);
}
