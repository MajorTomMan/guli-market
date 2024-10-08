package com.atguigu.gulimall.order.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.gulimall.order.vo.OrderItemVo;

@FeignClient("cart")
public interface CartFeignService {
    @GetMapping("/currentUserCartItems")
    @ResponseBody
    public List<OrderItemVo> getCurrentCartItems();
}
