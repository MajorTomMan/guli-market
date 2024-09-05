package com.atguigu.gulimall.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.gulimall.common.vo.CartItemVo;
import com.atguigu.gulimall.product.feign.CartFeignService;
import com.atguigu.gulimall.product.service.CartService;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartFeignService cartFeignService;
    @Override
    public List<CartItemVo> getCartItems() {
        // TODO Auto-generated method stub
        List<CartItemVo> currentCartItems = cartFeignService.getCurrentCartItems();
        return currentCartItems;
    }
    
}
