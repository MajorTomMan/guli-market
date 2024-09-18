package com.atguigu.gulimall.seckill.controller;

import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.seckill.service.SecKillService;
import com.atguigu.gulimall.seckill.to.SeckillSkuRedisTo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class SecKillController {
    @Autowired
    SecKillService secKillService;

    @GetMapping("/currentSeckillSkus")
    public R getCurrentSeckillSkus() {
        List<SeckillSkuRedisTo> vos = secKillService.getCurrentSeckillSkus();
        return R.ok().put("data", vos);
    }
    
}
