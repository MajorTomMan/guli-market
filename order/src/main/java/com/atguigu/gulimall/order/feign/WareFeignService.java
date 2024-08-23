package com.atguigu.gulimall.order.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.atguigu.gulimall.common.to.SkuHasStockVo;
import com.atguigu.gulimall.common.utils.R;

@FeignClient("ware")
public interface WareFeignService {
    @PostMapping("ware/waresku/hasstock")
    public R<List<SkuHasStockVo>> getSkuHasStock(@RequestBody List<Long> skuIds);
}
