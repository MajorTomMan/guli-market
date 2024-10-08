/*
 * @Date: 2024-09-18 17:58:55
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-09-18 21:14:18
 * @FilePath: \Guli\seckill\src\main\java\com\atguigu\gulimall\seckill\service\impl\SecKillServiceImpl.java
 * @Description: MajorTomMan @版权声明 保留文件所有权利
 */
package com.atguigu.gulimall.seckill.service.impl;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.atguigu.gulimall.common.to.mq.SeckillOrderTo;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.seckill.feign.CouponFeignService;
import com.atguigu.gulimall.seckill.feign.ProductFeignService;
import com.atguigu.gulimall.seckill.interceptor.LoginUserInterceptor;
import com.atguigu.gulimall.seckill.service.SecKillService;
import com.atguigu.gulimall.seckill.to.SeckillSkuRedisTo;
import com.atguigu.gulimall.seckill.vo.SeckillSessionWithSkusVo;
import com.atguigu.gulimall.seckill.vo.SkuInfoVo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class SecKillServiceImpl implements SecKillService {
    @Autowired
    CouponFeignService couponFeignService;
    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    Gson gson;
    @Autowired
    RabbitTemplate rabbitTemplate;
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
            if (data != null && !data.isEmpty()) {
                saveSessionInfos(data);
                saveSessionSkuInfos(data);
            }
        }
    }

    private void saveSessionSkuInfos(List<SeckillSessionWithSkusVo> vos) {
        vos.forEach(vo -> {
            BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            vo.getRelationEntities().forEach(entity -> {
                // 4. 随机码
                String token = UUID.randomUUID().toString().replace("=", ",");
                String key = entity.getPromotionSessionId().toString() + "-" + entity.getSkuId().toString();
                SeckillSkuRedisTo seckillSkuRedisTo = new SeckillSkuRedisTo();
                if (!redisTemplate.hasKey(key)) {

                    // 1. sku的基本信息
                    R r = productFeignService.getSkuInfo(entity.getSkuId());
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
                    seckillSkuRedisTo.setRandomCode(token);
                    boundHashOps.put(key, seckillSkuRedisTo);
                    // 5.使用库存作为分布式信号量
                    RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                    /*
                     * 商品可以秒杀的数量作为信号量
                     */
                    semaphore.trySetPermits(entity.getSeckillCount());
                }

            });
        });
    }

    private void saveSessionInfos(List<SeckillSessionWithSkusVo> vos) {
        /* 缓存活动信息 */
        vos.forEach(vo -> {
            long startTime = vo.getStartTime().getTime();
            long endTime = vo.getEndTime().getTime();
            String key = SESSION_CACHE_PREFIX + startTime + "_" + endTime;
            if (!redisTemplate.hasKey(key)) {
                List<String> list = vo.getRelationEntities().stream()
                        .map(item -> item.getPromotionSessionId() + "-" + item.getSkuId().toString()).toList();
                redisTemplate.opsForList().leftPushAll(key, list);
            }

        });
    }

    public static List<SeckillSkuRedisTo> seckillSkusHandler() {
        log.error("限流,降级处理");
        return null;
    }

    @SentinelResource(value = "getCurrentSeckillSkus", blockHandler = "seckillSkusHandler")
    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        // TODO Auto-generated method stub
        // 加try风格的限流
        try (Entry entry = SphU.entry("seckillSkus")) {
            Set<String> keys = redisTemplate.keys(SESSION_CACHE_PREFIX + "*");
            if (keys != null && keys.size() > 0) {
                long currentTime = System.currentTimeMillis();
                for (String key : keys) {
                    String replace = key.replace(SESSION_CACHE_PREFIX, "");
                    String[] split = replace.split("_");
                    long startTime = Long.parseLong(split[0]);
                    long endTime = Long.parseLong(split[1]);
                    // 当前秒杀活动处于有效期内
                    if (currentTime > startTime && currentTime < endTime) {
                        // 取出当前秒杀活动对应商品存储的hash key
                        List<Object> range = redisTemplate.opsForList().range(key, -100, 100);
                        List<Object> list = transList2String(range);
                        BoundHashOperations<String, Object, Object> ops = redisTemplate
                                .boundHashOps(SKUKILL_CACHE_PREFIX);
                        // 取出存储的商品信息并返回
                        List<Object> multiGet = ops.multiGet(list);
                        List<SeckillSkuRedisTo> collect = multiGet.stream().map(json -> {
                            SeckillSkuRedisTo redisTo = gson.fromJson(gson.toJson(json),
                                    SeckillSkuRedisTo.class);
                            return redisTo;
                        }).collect(Collectors.toList());
                        return collect;
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("秒杀商品被限流,原因:" + e.getMessage());
        }
        return null;
    }

    @Override
    public SeckillSkuRedisTo getSkuSecKillInfo(Long skuId) {
        // TODO Auto-generated method stub
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        // 获取所有商品的hash key
        Set<Object> keys = ops.keys();
        for (Object key : keys) {
            // 通过正则表达式匹配 数字-当前skuid的商品
            if (Pattern.matches("\\d-" + skuId, String.valueOf(key))) {
                Object v = ops.get(key);
                SeckillSkuRedisTo redisTo = gson.fromJson(gson.toJson(v), SeckillSkuRedisTo.class);
                // 当前商品参与秒杀活动
                if (redisTo != null) {
                    long current = System.currentTimeMillis();
                    // 当前活动在有效期，暴露商品随机码返回
                    if (redisTo.getStartTime() < current && redisTo.getEndTime() > current) {
                        return redisTo;
                    }
                    // 当前商品不再秒杀有效期，则隐藏秒杀所需的商品随机码
                    redisTo.setRandomCode(null);
                    return redisTo;
                }
            }
        }
        return null;
    }

    // TODO 设置商品的过期时间
    // TODO 秒杀后续的流程,简化了收货地址信息的计算
    @Override
    public String kill(String killId, String key, Integer num) {
        // TODO Auto-generated method stub
        Long startTime = System.currentTimeMillis();
        LinkedHashMap<String, Object> user = LoginUserInterceptor.loginUser.get();
        if (user == null) {
            log.error("用户未登录,返回");
            return null;
        }
        BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        Object json = boundHashOps.get(killId);
        if (json != null || !ObjectUtils.isEmpty(json)) {
            SeckillSkuRedisTo to = gson.fromJson(gson.toJson(json), SeckillSkuRedisTo.class);
            // 校验合法性
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            long time = new Date().getTime();
            log.info("start time :" + formatter.format(to.getStartTime()));
            log.info("current time:" + formatter.format(time));
            log.info("end time :" + formatter.format(to.getEndTime()));
            if (time >= to.getStartTime() && time <= to.getEndTime()) {
                // 校验随机码和商品id是否正确
                String randomCode = to.getRandomCode();
                String pid = to.getPromotionSessionId() + "-" + to.getSkuId();
                if (randomCode.equals(key) && killId.equals(pid)) {
                    // 校验购物数量是否合理
                    if (num <= to.getSeckillLimit()) {
                        // 验证是否重复购买,如果只要秒杀成功,就去占位
                        String redisKey = String.valueOf(user.get("id")) + "_" + to.getPromotionSessionId() + "_"
                                + to.getSkuId();
                        // 自动过期时间
                        long ttl = to.getEndTime() - time;
                        Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(redisKey, num, ttl,
                                TimeUnit.MILLISECONDS);
                        // 成功则说明该用户从未秒杀过该商品
                        if (setIfAbsent) {
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                            try {
                                boolean tryAcquire = semaphore.tryAcquire(num, Duration.ofMillis(1000));
                                if (tryAcquire) {
                                    // 快速下单,发送MQ消息
                                    String timeId = IdWorker.getTimeId();
                                    SeckillOrderTo seckillOrderTo = new SeckillOrderTo();
                                    seckillOrderTo.setOrderSn(timeId);
                                    seckillOrderTo.setMemberId(Long.valueOf((Integer) user.get("id")));
                                    seckillOrderTo.setNum(num);
                                    seckillOrderTo.setPromotionSessionId(to.getPromotionSessionId());
                                    seckillOrderTo.setSkuId(to.getSkuId());
                                    seckillOrderTo.setSeckillPrice(to.getSeckillPrice());
                                    rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.order",
                                            seckillOrderTo);
                                    Long endTime = System.currentTimeMillis();
                                    log.info("耗时时间:" + (endTime - startTime));
                                    return timeId;
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                // 若抛出异常则代表秒杀失败
                                log.error(e.getMessage());
                            }

                        }
                    }
                }
            }
        }
        return null;
    }

    // 用于解决range返回结果中list套娃的问题
    private List<Object> transList2String(List<Object> list) {
        List<Object> result = new ArrayList<>();
        if (list != null) {
            for (Object item : list) {
                if (item instanceof List) {
                    // 如果元素是一个嵌套的List，递归调用该方法并将其结果加入结果列表
                    result.addAll(transList2String((List<Object>) item));
                } else {
                    // 如果元素不是List，转换为字符串并加入结果列表
                    result.add(item);
                }
            }
        }
        return result;
    }
}
