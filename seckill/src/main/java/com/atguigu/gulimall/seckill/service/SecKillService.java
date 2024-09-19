package com.atguigu.gulimall.seckill.service;

import java.util.List;

import com.atguigu.gulimall.seckill.to.SeckillSkuRedisTo;

public interface SecKillService {

    void upload3Days();

    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    SeckillSkuRedisTo getSkuSecKillInfo(Long skuId);

    String kill(String killId, String key, Integer num);
}
