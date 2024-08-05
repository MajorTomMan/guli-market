package com.atguigu.gulimall.cart.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.atguigu.gulimall.cart.feign.ProductFeignService;
import com.atguigu.gulimall.cart.interceptor.CartInterceptor;
import com.atguigu.gulimall.cart.service.CartService;
import com.atguigu.gulimall.cart.to.UserInfoTo;
import com.atguigu.gulimall.cart.vo.CartItemVo;
import com.atguigu.gulimall.cart.vo.CartVo;
import com.atguigu.gulimall.cart.vo.SkuInfoVo;
import com.atguigu.gulimall.common.utils.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    ProductFeignService feignService;
    @Autowired
    ThreadPoolExecutor executor;
    @Autowired
    Gson gson;
    private final String CART_PREFIX = "gulimall:cart";

    @Override
    public CartItemVo addToCart(Long skuId, Integer num) {
        // TODO Auto-generated method stub

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String sku = (String) cartOps.get(skuId.toString());
        if (StringUtils.hasText(sku)) {
            CartItemVo cartItemVo = new CartItemVo();
            CompletableFuture.runAsync(() -> {
                R skuInfo = feignService.getSkuInfo(skuId);
                SkuInfoVo data = (SkuInfoVo) skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                cartItemVo.setCheck(true);
                cartItemVo.setCount(num);
                cartItemVo.setImage(data.getSkuDefaultImg());
                cartItemVo.setTitle(data.getSkuTitle());
                cartItemVo.setSkuId(skuId);
                cartItemVo.setPrice(data.getPrice());
                cartItemVo.setSkuAttrValues(null);
            }, executor).thenRun(() -> {
                List<String> values = feignService.getSkuSaleAttrValues(skuId);
                cartItemVo.setSkuAttrValues(values);
            }).join();
            String json = gson.toJson(cartItemVo);
            cartOps.put(skuId.toString(), json);
            return cartItemVo;
        } else {
            CartItemVo item = gson.fromJson(sku, CartItemVo.class);
            item.setCount(item.getCount() + num);
            String json = gson.toJson(item);
            cartOps.put(skuId.toString(), json);
            return item;
        }
    }

    @Override
    public CartItemVo getCartItem(Long skuId) {
        // TODO Auto-generated method stub
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String sku = (String) cartOps.get(skuId.toString());
        if (StringUtils.hasText(sku)) {
            CartItemVo item = gson.fromJson(sku, CartItemVo.class);
            return item;
        }
        return null;
    }

    @Override
    public CartVo getCart() {
        // TODO Auto-generated method stub
        CartVo cart = new CartVo();
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        List<Object> values = cartOps.values();
        if (values != null && !values.isEmpty()) {
            List<CartItemVo> collect = values.stream().map((obj) -> {
                return gson.fromJson((String) obj, CartItemVo.class);
            }).collect(Collectors.toList());
            cart.setItems(collect);
        }
        return cart;
    }

    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey = "";
        if (userInfoTo.getUserId() != null) {
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        } else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(cartKey);
        return boundHashOps;
    }
}
