package com.atguigu.gulimall.seckill.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.seckill.service.SecKillService;
import com.atguigu.gulimall.seckill.to.SeckillSkuRedisTo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class SecKillController {
    @Autowired
    SecKillService secKillService;

    @GetMapping("/getCurrentSeckillSkus")
    public R getCurrentSeckillSkus() {
        List<SeckillSkuRedisTo> vos = secKillService.getCurrentSeckillSkus();
        return R.ok().put("data", vos);
    }

    @GetMapping("sku/seckill/{skuId}")
    public R getSkuSecKillInfo(@PathVariable("skuId") Long skuId) {
        SeckillSkuRedisTo to = secKillService.getSkuSecKillInfo(skuId);
        return R.ok().put("data", to);
    }

    @GetMapping("/kill")
    public ModelAndView secKill(@RequestParam("killId") String killId, @RequestParam("key") String key,
            @RequestParam("num") Integer num, ModelAndView modelAndView) {
        String orderSn = null;
        try {
            // 1、判断是否登录
            orderSn = secKillService.kill(killId, key, num);
            modelAndView.setViewName("success");
            modelAndView.addObject("orderSn", orderSn);
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.setViewName("success");
        return modelAndView;
    }

}
