package com.atguigu.gulimall.seckill.service.impl;

import java.util.List;
import java.util.UUID;

import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.seckill.feign.CouponFeignService;
import com.atguigu.gulimall.seckill.feign.ProductFeignService;
import com.atguigu.gulimall.seckill.service.SecKillService;
import com.atguigu.gulimall.seckill.to.SeckillSkuRedisTo;
import com.atguigu.gulimall.seckill.vo.SeckillSessionWithSkusVo;
import com.atguigu.gulimall.seckill.vo.SeckillSkuVo;
import com.atguigu.gulimall.seckill.vo.SkuInfoVo;
import com.fasterxml.jackson.core.type.TypeReference;

public class SecKillServiceImpl implements SecKillService {
    @Autowired
    CouponFeignService couponFeignService;
    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    RedissonClient redissonClient;
    /* 前缀 */
    private final String SESSION_CACHE_PREFIX = "seckill:sessions:";
    private final String SKUKILL_CACHE_PREFIX = "seckill:skus:";
    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";

    @Override
    public void upload3Days() {
        // TODO Auto-generated method stub
        R r = couponFeignService.getLatest3Days();
        if (r.getCode() == 0) {
            List<SeckillSessionWithSkusVo> data = (List<SeckillSessionWithSkusVo>) r
                    .getData(new TypeReference<List<SeckillSessionWithSkusVo>>() {
                    });
            saveSessionInfos(data);
            saveSessionSkuInfos(data);
        }
    }

    private void saveSessionSkuInfos(List<SeckillSessionWithSkusVo> vos) {
        vos.forEach(vo -> {
            BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            vo.getRelationbEntities().forEach(entity -> {
                SeckillSkuRedisTo seckillSkuRedisTo = new SeckillSkuRedisTo();
                // 1. sku的基本信息
                R r = productFeignService.getSkuInfo(entity.getId());
                if (r.getCode() == 0) {
                    SkuInfoVo skuInfoVo = (SkuInfoVo) r.getData(new TypeReference<SkuInfoVo>() {
                    });
                    if (skuInfoVo != null) {
                        seckillSkuRedisTo.setSkuInfo(skuInfoVo);
                    }
                }
                // 2. sku的秒杀信息
                BeanUtils.copyProperties(entity, seckillSkuRedisTo);
                // 3. 设置商品的秒杀信息
                seckillSkuRedisTo.setStartTime(vo.getStartTime().getTime());
                seckillSkuRedisTo.setEndTime(vo.getEndTime().getTime());
                // 4. 随机码
                String token = UUID.randomUUID().toString().replace("=", ",");
                seckillSkuRedisTo.setRandomCode(token);
                // 5.使用库存作为分布式信号量
                RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                /*
                 * 商品可以秒杀的数量作为信号量
                 */
                semaphore.trySetPermits(entity.getSeckillCount());
                boundHashOps.put(entity.getSkuId().toString(), seckillSkuRedisTo);
            });
        });
    }

    private void saveSessionInfos(List<SeckillSessionWithSkusVo> vos) {
        /* 缓存活动信息 */
        vos.forEach(vo -> {
            long startTime = vo.getStartTime().getTime();
            long endTime = vo.getEndTime().getTime();
            String key = SESSION_CACHE_PREFIX + startTime + "_" + endTime;
            List<Long> list = vo.getRelationbEntities().stream().map(SeckillSkuVo::getSkuId).toList();
            redisTemplate.opsForList().leftPushAll(key, list);
        });
    }
}
