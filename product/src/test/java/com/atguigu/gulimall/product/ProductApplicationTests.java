package com.atguigu.gulimall.product;

import java.util.List;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductApplicationTests {
    @Autowired
    BrandService service;
    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        //brandEntity.setDescript("TTTTTTest");
        //brandEntity.setName("小米");
        //service.save(brandEntity);
        //System.out.println("Success!");
        brandEntity.setBrandId(14L);
        brandEntity.setDescript("Just for Test");
        service.updateById(brandEntity);
        System.out.println("Success!");
    }
    @Test
    void getAllInformation(){
        List<BrandEntity> list=service.list(new QueryWrapper<BrandEntity>());
        list.forEach((item)->{
            System.out.println(item);
        });
    }
}
