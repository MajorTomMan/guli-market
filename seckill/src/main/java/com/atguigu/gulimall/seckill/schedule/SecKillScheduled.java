/*
 * @Date: 2024-09-18 10:29:10
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-09-18 20:49:37
 * @FilePath: \Guli\seckill\src\main\java\com\atguigu\gulimall\seckill\schedule\SecKillScheduled.java
 * @Description: MajorTomMan @版权声明 保留文件所有权利
 */
package com.atguigu.gulimall.seckill.schedule;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.atguigu.gulimall.seckill.service.SecKillService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class SecKillScheduled {
    @Autowired
    SecKillService secKillService;
    @Autowired
    RedissonClient redissonClient;
    private final String upload_lock = "seckill:upload:lock";

    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadSecKillSkuLatest3Days() {
        // 分布式锁
        RLock lock = redissonClient.getLock(upload_lock);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            secKillService.upload3Days();
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage());
        } finally {
            lock.unlock();
        }

    }
}
