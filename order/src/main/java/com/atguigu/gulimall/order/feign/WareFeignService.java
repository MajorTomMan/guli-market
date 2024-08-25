package com.atguigu.gulimall.order.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.gulimall.common.to.SkuHasStockVo;
import com.atguigu.gulimall.common.utils.R;

@FeignClient("ware")
public interface WareFeignService {
    @PostMapping("ware/waresku/hasstock")
    public R<List<SkuHasStockVo>> getSkuHasStock(@RequestBody List<Long> skuIds);

    @GetMapping("ware/waresku/getFare")
    public R getFare(@RequestParam("addrId") Long addrId);
}
