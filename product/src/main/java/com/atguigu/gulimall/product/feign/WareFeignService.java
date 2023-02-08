package com.atguigu.gulimall.product.feign;

import com.atguigu.gulimall.common.utils.R;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("ware")
public interface WareFeignService {
    @RequestMapping("ware/waresku/info/{id}")
    R queryStock(@PathVariable("id") Long id);
}
