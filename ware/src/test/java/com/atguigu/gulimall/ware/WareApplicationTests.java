package com.atguigu.gulimall.ware;

import java.util.List;

import com.atguigu.gulimall.ware.entity.PurchaseEntity;
import com.atguigu.gulimall.ware.service.PurchaseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WareApplicationTests {
    @Autowired
    PurchaseService service;
    @Test
    void contextLoads() {
        List<PurchaseEntity> list=service.list(new QueryWrapper<PurchaseEntity>());
        list.forEach((item)->{
            System.out.println(item);
        });
    }

}
