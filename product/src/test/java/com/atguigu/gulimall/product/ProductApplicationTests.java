/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-07-21 16:08:04
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-09-15 22:58:09
 * @FilePath: /GuliMall/product/src/test/java/com/atguigu/gulimall/product/ProductApplicationTests.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.product;

import java.util.List;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class ProductApplicationTests {
    @Autowired
    BrandService service;
    @Autowired
    CategoryService categoryService;
    @Autowired
    StringRedisTemplate redis;
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    BrandService brandService;
    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        // brandEntity.setDescript("TTTTTTest");
        // brandEntity.setName("小米");
        // service.save(brandEntity);
        // System.out.println("Success!");
        brandEntity.setDescript("Just for Test");
        service.updateById(brandEntity);
        System.out.println("Success!");
    }
    @Test
    public void testRediss(){
        System.out.println(redissonClient);
    }
    @Test
    void getAllInformation() {
        List<BrandEntity> list = service.list(new QueryWrapper<BrandEntity>());
        list.forEach((item) -> {
            System.out.println(item);
        });
    }

    @Test
    void testPath() {
        Long[] paths = categoryService.findCateLogPath(225L);
        log.info("完整路径:{}", Arrays.asList(paths));
        System.out.println();
    }
    @Test
    void testRedis(){
        ValueOperations<String, String> ops = redis.opsForValue();
        ops.set("key","value",60);
    }
    @Test
    void testInsert(){
        BrandEntity entity=new BrandEntity();
        entity.setName("测试");
        brandService.save(entity);
    }
}
