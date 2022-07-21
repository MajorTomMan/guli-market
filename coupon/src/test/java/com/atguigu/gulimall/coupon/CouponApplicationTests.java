package com.atguigu.gulimall.coupon;

import java.util.List;

import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.atguigu.gulimall.coupon.service.CouponService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponApplicationTests {
    @Autowired
    CouponService service;
    @Test
    void contextLoads() {
        List<CouponEntity> list=service.list(new QueryWrapper<CouponEntity>());
        list.forEach((item)->{
            System.out.println(item);
        });
    }

}
