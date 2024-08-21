package com.atguigu.gulimall.cart.service;

import java.util.List;

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

    void countItem(Long skuId, Integer num);

    List<CartItemVo> getUserCartItems();
}
