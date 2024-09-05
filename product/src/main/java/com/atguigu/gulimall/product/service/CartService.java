package com.atguigu.gulimall.product.service;

import java.util.List;

import com.atguigu.gulimall.common.vo.CartItemVo;

public interface CartService {

    List<CartItemVo> getCartItems();

}