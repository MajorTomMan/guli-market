package com.atguigu.gulimall.cart.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
import com.atguigu.gulimall.common.constant.CartConstant;
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
    ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    Gson gson;

    @Override
    public CartItemVo addToCart(Long skuId, Integer num) {

        // 拿到要操作的购物车信息
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        // 判断Redis是否有该商品的信息
        LinkedHashMap<String, Object> sku = (LinkedHashMap) cartOps.get(skuId.toString());
        // 如果没有就添加数据
        if (sku == null) {
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
            }, threadPoolExecutor).thenRun(() -> {
                List<String> values = feignService.getSkuSaleAttrValues(skuId);
                cartItemVo.setSkuAttrValues(values);
            }).join();
            cartOps.put(skuId.toString(), cartItemVo);
            return cartItemVo;
        } else {
            CartItemVo item = gson.fromJson(gson.toJson(sku), CartItemVo.class);
            item.setCount(item.getCount() + num);
            cartOps.put(skuId.toString(), item);
            return item;
        }
    }

    @Override
    public CartItemVo getCartItem(Long skuId) {

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        LinkedHashMap<String, Object> sku = (LinkedHashMap) cartOps.get(skuId.toString());
        if (sku != null && !sku.isEmpty()) {
            CartItemVo item = gson.fromJson(gson.toJson(sku), CartItemVo.class);
            return item;
        }
        return null;
    }

    @Override
    public CartVo getCart() {

        CartVo cartVo = new CartVo();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() != null) {
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
            List<CartItemVo> tempCartItems = getCartItems(CartConstant.CART_PREFIX + userInfoTo.getUserKey());
            if (tempCartItems != null) {
                tempCartItems.forEach((item) -> {
                        addToCart(item.getSkuId(), item.getCount());
                });
                // 清除临时购物车
                cleanItems(CartConstant.CART_PREFIX + userInfoTo.getUserKey());
            }
            List<CartItemVo> cartItems = getCartItems(cartKey);
            if (cartItems != null && !cartItems.isEmpty()) {
                cartVo.setItems(cartItems);
            } else {
                cartVo.setItems(new ArrayList<>());
            }
            return cartVo;
        } else {
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
            List<CartItemVo> cartItems = getCartItems(cartKey);
            if (cartItems != null && !cartItems.isEmpty()) {
                cartVo.setItems(cartItems);
            } else {
                cartVo.setItems(new ArrayList<>());
            }
        }
        List<CartItemVo> items = cartVo.getItems();
        if (!items.isEmpty()) {
            // 计算总价
            BigDecimal totalAmount = items.stream().map(item -> {
                return item.getTotalPrice();
            }).reduce(BigDecimal.ZERO, BigDecimal::add);
            cartVo.setTotalAmount(totalAmount);
            // 计算总数量
            int totalNumber = items.stream().mapMultiToInt((item, consumer) -> {
                consumer.accept(item.getCount());
            }).sum();
            cartVo.setCountNum(totalNumber);
        } else {
            cartVo.setTotalAmount(BigDecimal.ZERO);
            cartVo.setCountNum(0);
        }
        return cartVo;
    }

    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey = "";
        if (userInfoTo.getUserId() != null) {
            cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
        } else {
            cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
        }
        BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(cartKey);
        return boundHashOps;
    }

    private List<CartItemVo> getCartItems(String cartKey) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        List<Object> values = cartOps.values();
        if (values != null && !values.isEmpty()) {
            List<CartItemVo> collect = values.stream().map((obj) -> {
                return gson.fromJson(gson.toJson(obj), CartItemVo.class);
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    public void cleanItems(String[] cartKeys) {
        for (String key : cartKeys) {
            cleanItems(key);
        }

    }

    public void cleanItems(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    @Override
    public void deleteItem(Long skuId) {

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        LinkedHashMap<String, Object> sku = (LinkedHashMap) cartOps.get(skuId.toString());
        if (sku != null && !sku.isEmpty()) {
            cartOps.delete(skuId.toString());
        }
    }

    @Override
    public void checkCart(Integer isChecked, Long skuId) {

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        LinkedHashMap<String, Object> sku = (LinkedHashMap) cartOps.get(skuId.toString());
        CartItemVo cartItemVo = gson.fromJson(gson.toJson(sku), CartItemVo.class);
        cartItemVo.setCheck(isChecked == 1);
        cartOps.put(skuId.toString(), cartItemVo);
    }

    @Override
    public void countItem(Long skuId, Integer num) {

        if (num == 0) {
            deleteItem(skuId);
            return;
        }
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        LinkedHashMap<String, Object> sku = (LinkedHashMap) cartOps.get(skuId.toString());
        CartItemVo cartItemVo = gson.fromJson(gson.toJson(sku), CartItemVo.class);
        cartItemVo.setCount(num);
        cartItemVo.setTotalPrice(cartItemVo.getTotalPrice());
        cartOps.put(skuId.toString(), cartItemVo);
    }

    @Override
    public List<CartItemVo> getUserCartItems() {

        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() == null) {
            return null;
        } else {
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
            List<CartItemVo> cartItems = getCartItems(cartKey);
            return cartItems.stream().filter(item -> {
                return item.getCheck();
            }).map(item -> {
                BigDecimal price = feignService.getPrice(item.getSkuId());
                item.setPrice(price);
                return item;
            }).toList();
        }
    }
}
