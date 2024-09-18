package com.atguigu.gulimall.seckill.schedule;

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
    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadSecKillSkuLatest3Days() {
        secKillService.upload3Days();
    }
}
