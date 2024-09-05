package com.atguigu.gulimall.order;

import java.util.List;

import com.atguigu.gulimall.order.entity.OrderEntity;
import com.atguigu.gulimall.order.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest
class OrderApplicationTests {
    @Autowired
    OrderService service;
    @Test
    void contextLoads() {
        List<OrderEntity> list=service.list(new QueryWrapper<OrderEntity>());
        list.forEach((item)->{
            System.out.println(item);
        });
    }


}
