package com.atguigu.gulimall.coupon;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.atguigu.gulimall.coupon.controller.SeckillPromotionController;
import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.atguigu.gulimall.coupon.entity.SeckillSessionEntity;
import com.atguigu.gulimall.coupon.service.CouponService;
import com.atguigu.gulimall.coupon.service.SeckillSessionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponApplicationTests {
    @Autowired
    CouponService service;
    @Autowired
    SeckillSessionService seckillSessionService;

    @Test
    void contextLoads() {
        List<CouponEntity> list = service.list(new QueryWrapper<CouponEntity>());
        list.forEach((item) -> {
            System.out.println(item);
        });
        System.out.println("Success!");
    }

    @Test
    void testUpdate() {
        SeckillSessionEntity seckillSessionEntity = new SeckillSessionEntity();
        seckillSessionEntity.setId(2L);
        seckillSessionEntity.setEndTime(endTime());
        seckillSessionEntity.setStartTime(startTime());
        seckillSessionService.updateById(seckillSessionEntity);
    }

    private Date endTime() {
        LocalDateTime now = LocalDateTime.now().plusDays(100);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date startTime() {
        LocalDateTime now = LocalDateTime.now();
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

}
