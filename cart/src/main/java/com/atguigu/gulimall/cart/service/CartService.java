package com.atguigu.gulimall.cart.service;

import com.atguigu.gulimall.cart.vo.CartItemVo;
import com.atguigu.gulimall.cart.vo.CartVo;

public interface CartService {

    CartItemVo addToCart(Long skuId, Integer num);

    CartItemVo getCartItem(Long skuId);

    CartVo getCart();

    void cleanItems(String[] keys);

    void cleanItems(String key);

    void deleteItem(Long skuId);

    void checkCart(Integer isChecked, Long skuId);
}
