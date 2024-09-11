package com.atguigu.gulimall.member.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.gulimall.common.utils.R;

@FeignClient("order")
public interface OrderFeignService {
    @RequestMapping("order/order/listWithItem")
    // @RequiresPermissions("order:order:list")
    public R listWithItem(@RequestBody Map<String, Object> params);
}
